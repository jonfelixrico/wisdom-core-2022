package com.wisdom.quote.projection;

import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;

import com.eventstore.dbclient.ResolvedEvent;
import com.eventstore.dbclient.SubscribeToAllOptions;
import com.eventstore.dbclient.Subscription;
import com.eventstore.dbclient.SubscriptionListener;
import com.wisdom.eventstoredb.EventStoreDBProvider;
import com.wisdom.eventstoredb.checkpoint.PositionCheckpointService;
import com.wisdom.quote.projection.snapshot.QuoteSnapshotRepository;

class QuotePassiveProjectionService {
	private static final Logger LOGGER = LoggerFactory.getLogger(QuotePassiveProjectionService.class);
	private static final String POSITION_CHECKPOINT_ID = "quote";

	@Autowired
	private EventStoreDBProvider provider;

	@Autowired
	private PositionCheckpointService posSvc;
	
	@Autowired
	private QuoteEventsReducer reducer;
	
	@Autowired
	private QuoteSnapshotRepository snapshotRepo;
	
	private SubscriptionListener listener = new SubscriptionListener() {
		@Override
		public void onEvent(Subscription subscription, ResolvedEvent event) {
			handleEvent(event);
		}
	};
	
	private void handleEvent (ResolvedEvent resolvedEvent) {
		var event = resolvedEvent.getEvent();
		
		try {
			var eventClass = QuoteEventsReducer.EVENT_TYPE_TO_EVENT_CLASS.get(event.getEventType());
			if (eventClass == null) {
				// TODO throw exception
				LOGGER.warn("No event class mapped to event type {}!", event.getEventType());
				return;
			}
			
			// TODO continue this
		} finally {
			posSvc.setPosition(POSITION_CHECKPOINT_ID, event.getPosition());
		}
	}

	@EventListener(ApplicationStartedEvent.class)
	private void onStart() throws InterruptedException, ExecutionException {
		SubscribeToAllOptions options = SubscribeToAllOptions.get();
		options.fromPosition(posSvc.getPosition(POSITION_CHECKPOINT_ID));
		provider.getClient().subscribeToAll(listener, options);
	}
}
