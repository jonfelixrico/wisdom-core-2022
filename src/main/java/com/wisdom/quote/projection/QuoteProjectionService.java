package com.wisdom.quote.projection;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eventstore.dbclient.ReadResult;
import com.eventstore.dbclient.ReadStreamOptions;
import com.eventstore.dbclient.RecordedEvent;
import com.eventstore.dbclient.ResolvedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wisdom.eventstoredb.EventStoreDBProvider;
import com.wisdom.quote.entity.QuoteEntity;

@Service
public class QuoteProjectionService {
	private static final Logger LOGGER = LoggerFactory.getLogger(QuoteProjectionService.class);

	@Autowired
	private EventStoreDBProvider esdbProvider;

	@Autowired
	private QuoteEventsReducer reducer;

	@Autowired
	private QuoteSnapshotService snapshotRepo;

	@Autowired
	private ObjectMapper mapper;

	public QuoteProjection getProjection(String quoteId)
			throws InterruptedException, ExecutionException, IOException {
		var snapshot = snapshotRepo.get(quoteId);

		if (snapshot != null) {
			return buildState(quoteId, snapshot.getRevision(), snapshot);
		}

		var projection = buildState(quoteId, null, null);
		snapshotRepo.save(projection, projection.getRevision());
		return projection;
	}

	private QuoteProjection buildState(String quoteId, Long fromRevision,
			QuoteEntity baseModel) throws InterruptedException, ExecutionException, IOException {
		ReadStreamOptions options = ReadStreamOptions.get();
		if (fromRevision == null) {
			options.fromStart();
		} else {
			options.fromRevision(fromRevision);
		}

		var state = baseModel;
		var revision = fromRevision;

		LOGGER.debug("Reading quote {} starting from revision {}", quoteId, fromRevision);
		ReadResult results = esdbProvider.getClient().readStream(String.format("quote/%s", quoteId), options).get();
		for (ResolvedEvent result : results.getEvents()) {
			RecordedEvent event = result.getEvent();

			var eventClass = reducer.getEventClassFromType(event.getEventType());
			if (eventClass == null) {
				// TODO throw exception
				LOGGER.warn("No event class mapped to event type {}!", event.getEventType());
				continue;
			}

			var eventData = mapper.readValue(event.getEventData(), eventClass);
			state = reducer.reduceEvent(baseModel, eventData);
			revision = event.getStreamRevision().getValueUnsigned();
		}

		return new QuoteProjectionImpl(state, revision);
	}
}
