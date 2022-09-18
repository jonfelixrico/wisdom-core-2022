package com.wisdom.quote.projection;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;

import com.eventstore.dbclient.ReadAllOptions;
import com.eventstore.dbclient.ResolvedEvent;
import com.eventstore.dbclient.SubscribeToAllOptions;
import com.eventstore.dbclient.Subscription;
import com.eventstore.dbclient.SubscriptionListener;
import com.wisdom.eventstoredb.EventStoreDBProvider;
import com.wisdom.eventstoredb.checkpoint.PositionCheckpointService;

class QuotePassiveProjectionService {
	private static final String POSITION_CHECKPOINT_ID = "quote";

	@Autowired
	private EventStoreDBProvider provider;

	@Autowired
	private PositionCheckpointService posSvc;
	
	private SubscriptionListener listener = new SubscriptionListener() {
		@Override
		public void onEvent(Subscription subscription, ResolvedEvent event) {
			// TODO Auto-generated method stub
			super.onEvent(subscription, event);
			posSvc.getPosition("hi");
		}
	};

	@EventListener(ApplicationStartedEvent.class)
	private void onStart() throws InterruptedException, ExecutionException {
		SubscribeToAllOptions options = SubscribeToAllOptions.get();
		options.fromPosition(posSvc.getPosition(POSITION_CHECKPOINT_ID));
		provider.getClient().subscribeToAll(listener, options);
	}
}
