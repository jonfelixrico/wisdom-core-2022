package com.wisdom.quote.projection;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import com.eventstore.dbclient.ReadResult;
import com.eventstore.dbclient.ReadStreamOptions;
import com.eventstore.dbclient.RecordedEvent;
import com.eventstore.dbclient.ResolvedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wisdom.common.writemodel.Event;
import com.wisdom.eventstoredb.EventStoreDBProvider;
import com.wisdom.quote.mongodb.QuoteMongoModel;
import com.wisdom.quote.mongodb.QuoteMongoRepository;
import com.wisdom.quote.writemodel.events.QuoteApprovedBySystemEvent;
import com.wisdom.quote.writemodel.events.QuoteFlaggedAsExpiredBySystemEvent;
import com.wisdom.quote.writemodel.events.QuoteReceivedEvent;
import com.wisdom.quote.writemodel.events.QuoteSubmittedEvent;
import com.wisdom.quote.writemodel.events.QuoteVotesModifiedEvent;

@Service
public class QuoteProjectionService {
	private static final Logger LOGGER = LoggerFactory.getLogger(QuoteProjectionService.class);

	@Autowired
	private EventStoreDBProvider esdbProvider;

	@Autowired
	private QuoteEventsReducer reducer;

	@Autowired
	QuoteMongoRepository repo;

	@Autowired
	MongoTemplate template;

	@Autowired
	ObjectMapper mapper;

	private final Map<String, Class<? extends Event>> EVENT_TYPE_TO_EVENT_CLASS = Map.of(QuoteSubmittedEvent.EVENT_TYPE,
			QuoteSubmittedEvent.class, QuoteReceivedEvent.EVENT_TYPE, QuoteReceivedEvent.class,
			QuoteFlaggedAsExpiredBySystemEvent.EVENT_TYPE, QuoteFlaggedAsExpiredBySystemEvent.class,
			QuoteApprovedBySystemEvent.EVENT_TYPE, QuoteApprovedBySystemEvent.class, QuoteVotesModifiedEvent.EVENT_TYPE,
			QuoteVotesModifiedEvent.class);

	public Pair<QuoteProjectionModel, Long> getProjection(String quoteId)
			throws InterruptedException, ExecutionException, IOException {
		var snapshot = getSnapshot(quoteId);

		if (snapshot != null) {
			return buildState(quoteId, snapshot.getSecond(), snapshot.getFirst());
		}

		var projection = buildState(quoteId, null, null);
		saveSnapshot(projection.getFirst(), projection.getSecond());
		return projection;
	}

	private Pair<QuoteProjectionModel, Long> buildState(String quoteId, Long fromRevision,
			QuoteProjectionModel baseModel) throws InterruptedException, ExecutionException, IOException {
		ReadStreamOptions options = ReadStreamOptions.get();
		if (fromRevision == null) {
			options.fromStart();
		} else {
			options.fromRevision(fromRevision);
		}

		Pair<QuoteProjectionModel, Long> state = baseModel == null ? null : Pair.of(baseModel, fromRevision);

		LOGGER.debug("Reading quote {} starting from revision {}", quoteId, fromRevision);
		ReadResult results = esdbProvider.getClient().readStream(String.format("quote/%s", quoteId), options).get();
		for (ResolvedEvent result : results.getEvents()) {
			RecordedEvent event = result.getEvent();

			var eventClass = EVENT_TYPE_TO_EVENT_CLASS.get(event.getEventType());
			if (eventClass == null) {
				// TODO throw exception
				LOGGER.warn("No event class mapped to event type {}!", event.getEventType());
				continue;
			}

			var eventData = mapper.readValue(event.getEventData(), eventClass);
			state = Pair.of(reducer.reduce(baseModel, eventData), event.getStreamRevision().getValueUnsigned());
		}

		return state;
	}

	private static Pair<QuoteProjectionModel, Long> convertMongoModelToProjectionModel(QuoteMongoModel input) {
		QuoteProjectionModel projModel = new QuoteProjectionModel(input.getId(), input.getContent(),
				input.getAuthorId(), input.getSubmitterId(), input.getSubmitDt(), input.getExpirationDt(),
				input.getServerId(), input.getChannelId(), input.getMessageId(), input.getVoterIds(),
				input.getReceives(), input.getVerdict(), input.getRequiredVoteCount());

		return Pair.of(projModel, input.getRevision());
	}

	private Pair<QuoteProjectionModel, Long> getSnapshot(String quoteId) {
		var result = repo.findById(quoteId);
		if (result.isEmpty()) {
			return null;
		}

		return convertMongoModelToProjectionModel(result.get());
	}

	private static QuoteMongoModel convertProjectionModelToMongoModel(QuoteProjectionModel model, long revision) {
		return new QuoteMongoModel(model.getId(), model.getContent(), model.getAuthorId(), model.getSubmitterId(),
				model.getSubmitDt(), model.getExpirationDt(), model.getServerId(), model.getChannelId(),
				model.getMessageId(), model.getVoterIds(), model.getReceives(), model.getVerdict(), model.getRequiredVoteCount(), revision);
	}

	private void saveSnapshot(QuoteProjectionModel model, long revision) {
		var mongoModel = convertProjectionModelToMongoModel(model, revision);
		if (!repo.existsById(model.getId())) {
			repo.insert(mongoModel);
			LOGGER.debug("Created snapshot of quote {} revision {}", model.getId(), revision);
			return;
		}

		Query query = new Query().addCriteria(Criteria.where("id").is(model.getId()).and("revision").lt(revision));
		var result = template.findAndReplace(query, mongoModel);

		if (result != null) {
			LOGGER.debug("Updated the snapshot of quote {} revision {}", model.getId(), revision);
		}
	}
}
