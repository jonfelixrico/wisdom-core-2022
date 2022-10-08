package com.wisdom.quote.readmodel;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.eventstore.dbclient.ResolvedEvent;
import com.eventstore.dbclient.SubscribeToAllOptions;
import com.eventstore.dbclient.Subscription;
import com.eventstore.dbclient.SubscriptionFilter;
import com.eventstore.dbclient.SubscriptionListener;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.wisdom.eventstoredb.EventStoreDBProvider;

@Service
class QuoteReadModelCatchUp {
	private static final Logger LOGGER = LoggerFactory.getLogger(QuoteReadModelCatchUp.class);

	@Autowired
	private QuoteReadModelReducer reducer;

	@Autowired
	private EventStoreDBProvider esdb;
	
	private void catchUpLaggingModel (LaggingRevisionException cause) {
		// TODO implement this
		LOGGER.error("Encoutnered LRE but it is still NOOP", cause);
	}

	private void processEvent(ResolvedEvent event) throws StreamReadException, DatabindException, IOException {
		var recordedEvt = event.getEvent();
		try {
			reducer.reduce(recordedEvt);
		} catch (LaggingRevisionException e) {
			catchUpLaggingModel(e);
		} catch (UnrecognizedEventTypeException e) {
			LOGGER.debug("Skipped unrecognized event type {}", e.getEventType());
		}
	}

	private SubscriptionListener getListener() {
		return new SubscriptionListener() {
			@Override
			public void onEvent(Subscription subscription, ResolvedEvent event) {
				try {
					processEvent(event);
				} catch (Exception e) {
					LOGGER.error("Error encountered during catch-up", e);
				}
			}
		};
	}

	private SubscribeToAllOptions getOptions() {
		SubscriptionFilter filter = SubscriptionFilter.newBuilder().withStreamNamePrefix("quote/")
				.withEventTypePrefix("QUOTE_").build();
		return SubscribeToAllOptions.get().filter(filter);
	}

	@EventListener
	private void startCatchUp(ApplicationStartedEvent ctx) throws InterruptedException, ExecutionException {
		esdb.getClient().subscribeToAll(getListener(), getOptions());
	}
}
