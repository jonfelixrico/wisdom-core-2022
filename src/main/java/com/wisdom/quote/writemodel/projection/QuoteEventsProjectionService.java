package com.wisdom.quote.writemodel.projection;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;

import com.eventstore.dbclient.EventStoreDBClient;
import com.eventstore.dbclient.ReadResult;
import com.eventstore.dbclient.ReadStreamOptions;
import com.eventstore.dbclient.RecordedEvent;
import com.eventstore.dbclient.ResolvedEvent;
import com.wisdom.eventsourcing.Event;
import com.wisdom.quote.writemodel.events.QuoteApprovedBySystemEvent;
import com.wisdom.quote.writemodel.events.QuoteFlaggedAsExpiredBySystemEvent;
import com.wisdom.quote.writemodel.events.QuoteReceivedEvent;
import com.wisdom.quote.writemodel.events.QuoteSubmittedEvent;
import com.wisdom.quote.writemodel.events.QuoteVoteAddedEvent;
import com.wisdom.quote.writemodel.events.QuoteVoteRemovedEvent;

public class QuoteEventsProjectionService { 
	private static final Logger LOGGER = LoggerFactory.getLogger(QuoteEventsProjectionService.class);
	
	@Autowired
	private EventStoreDBClient client;
	
	@Autowired
	private QuoteEventsReducer reducer;
	
	private final Map<String, Class<? extends Event>> EVENT_TYPE_TO_EVENT_CLASS = Map.of(
			QuoteSubmittedEvent.EVENT_TYPE, QuoteSubmittedEvent.class,
			QuoteReceivedEvent.EVENT_TYPE, QuoteReceivedEvent.class,
			QuoteFlaggedAsExpiredBySystemEvent.EVENT_TYPE, QuoteFlaggedAsExpiredBySystemEvent.class,
			QuoteApprovedBySystemEvent.EVENT_TYPE, QuoteApprovedBySystemEvent.class,
			QuoteVoteAddedEvent.EVENT_TYPE, QuoteVoteAddedEvent.class,
			QuoteVoteRemovedEvent.EVENT_TYPE, QuoteVoteRemovedEvent.class
	);


	public Pair<QuoteProjectionModel, Long> buildState (String quoteId, long fromRevision, QuoteProjectionModel baseModel) throws InterruptedException, ExecutionException, IOException {
		ReadStreamOptions options = ReadStreamOptions.get();
		options.fromRevision(fromRevision);
		
		Pair<QuoteProjectionModel, Long> state = Pair.of(baseModel, fromRevision);
		
		ReadResult results = client.readStream(String.format("quote/%s", quoteId), options).get();
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
}

