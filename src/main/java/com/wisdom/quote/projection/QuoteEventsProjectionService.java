package com.wisdom.quote.projection;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import com.eventstore.dbclient.ReadResult;
import com.eventstore.dbclient.ReadStreamOptions;
import com.eventstore.dbclient.RecordedEvent;
import com.eventstore.dbclient.ResolvedEvent;
import com.wisdom.eventsourcing.Event;
import com.wisdom.eventstoredb.EventStoreDBProvider;
import com.wisdom.quote.mongodb.QuoteMongoModel;
import com.wisdom.quote.mongodb.QuoteMongoRepository;
import com.wisdom.quote.writemodel.events.QuoteApprovedBySystemEvent;
import com.wisdom.quote.writemodel.events.QuoteFlaggedAsExpiredBySystemEvent;
import com.wisdom.quote.writemodel.events.QuoteReceivedEvent;
import com.wisdom.quote.writemodel.events.QuoteSubmittedEvent;
import com.wisdom.quote.writemodel.events.QuoteVoteAddedEvent;
import com.wisdom.quote.writemodel.events.QuoteVoteRemovedEvent;

@Service
public class QuoteEventsProjectionService { 
	private static final Logger LOGGER = LoggerFactory.getLogger(QuoteEventsProjectionService.class);
	
	@Autowired
	private EventStoreDBProvider esdbProvider;
	
	@Autowired
	private QuoteEventsReducer reducer;
	
	@Autowired
	
	private final Map<String, Class<? extends Event>> EVENT_TYPE_TO_EVENT_CLASS = Map.of(
			QuoteSubmittedEvent.EVENT_TYPE, QuoteSubmittedEvent.class,
			QuoteReceivedEvent.EVENT_TYPE, QuoteReceivedEvent.class,
			QuoteFlaggedAsExpiredBySystemEvent.EVENT_TYPE, QuoteFlaggedAsExpiredBySystemEvent.class,
			QuoteApprovedBySystemEvent.EVENT_TYPE, QuoteApprovedBySystemEvent.class,
			QuoteVoteAddedEvent.EVENT_TYPE, QuoteVoteAddedEvent.class,
			QuoteVoteRemovedEvent.EVENT_TYPE, QuoteVoteRemovedEvent.class
	);


	private Pair<QuoteProjectionModel, Long> buildState (String quoteId, long fromRevision, QuoteProjectionModel baseModel) throws InterruptedException, ExecutionException, IOException {
		ReadStreamOptions options = ReadStreamOptions.get();
		options.fromRevision(fromRevision);
		
		Pair<QuoteProjectionModel, Long> state = Pair.of(baseModel, fromRevision);
		
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
			
			var eventData = event.getEventDataAs(eventClass);
			state = Pair.of(reducer.reduce(baseModel, eventData), event.getStreamRevision().getValueUnsigned());
		}
		
		return state;
	}
	
	QuoteMongoRepository repo;

	private static Pair<QuoteProjectionModel, Long> convertMongoModelToSnapshotModel(QuoteMongoModel input) {
		Map<String, Vote> votes = input.getVotes().stream().collect(Collectors.toMap(v -> v.getUserId(), v -> v));
		QuoteProjectionModel projModel = new QuoteProjectionModel(input.getId(), input.getContent(),
				input.getAuthorId(), input.getSubmitterId(), input.getSubmitDt(), input.getExpirationDt(),
				input.getServerId(), input.getChannelId(), input.getMessageId(), votes, input.getReceives(),
				input.getVerdict());

		return Pair.of(projModel, input.getRevision());
	}
	
	private Pair<QuoteProjectionModel, Long> getSnapshot(String quoteId) {
		var result = repo.findById(quoteId);
		if (result.isEmpty()) {
			return null;
		}
		
		return convertMongoModelToSnapshotModel(result.get());
	}
}

