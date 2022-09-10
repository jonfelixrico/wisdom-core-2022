package com.wisdom.quote.writemodel.projection;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;

import com.eventstore.dbclient.EventStoreDBClient;
import com.eventstore.dbclient.ReadResult;
import com.eventstore.dbclient.ReadStreamOptions;
import com.eventstore.dbclient.RecordedEvent;
import com.eventstore.dbclient.ResolvedEvent;
import com.wisdom.quote.writemodel.events.QuoteReceivedEvent;

public class QuoteEventsProjectionService {
	@Autowired
	private EventStoreDBClient client;
	
	private Class<?> getClass(String eventType) {
		switch (eventType) {
			case QuoteReceivedEvent.EVENT_TYPE:
				return QuoteReceivedEvent.class;
		}
		
		return null;
	}

	public Pair<QuoteProjectionModel, Long> computeCurrentState (String quoteId, long fromRevision, QuoteProjectionModel baseModel) throws InterruptedException, ExecutionException {
		ReadStreamOptions options = ReadStreamOptions.get();
		options.fromRevision(fromRevision);
		
		ReadResult results = client.readStream(String.format("quote/%s", quoteId), options).get();
		for (ResolvedEvent result : results.getEvents()) {
			RecordedEvent event = result.getEvent();
			
		}
	}
}

