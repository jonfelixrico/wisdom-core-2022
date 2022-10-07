package com.wisdom.quote.readmodel;

import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.eventstore.dbclient.RecordedEvent;
import com.eventstore.dbclient.ResolvedEvent;
import com.eventstore.dbclient.SubscribeToAllOptions;
import com.eventstore.dbclient.Subscription;
import com.eventstore.dbclient.SubscriptionFilter;
import com.eventstore.dbclient.SubscriptionListener;
import com.wisdom.eventstoredb.EventStoreDBProvider;

@Service
class QuoteReadModelCatchUp {
	private static final Logger LOGGER = LoggerFactory.getLogger(QuoteReadModelCatchUp.class);

	@Autowired
	private QuoteReadModelReducer reducer;

	@Autowired
	private EventStoreDBProvider esdb;

	private void processEvent(ResolvedEvent event) {
		// TODO implement this
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
