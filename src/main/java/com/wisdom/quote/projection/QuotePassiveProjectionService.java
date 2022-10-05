package com.wisdom.quote.projection;

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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wisdom.eventstoredb.EventStoreDBProvider;
import com.wisdom.eventstoredb.checkpoint.PositionCheckpointService;
import com.wisdom.quote.projection.snapshot.QuoteSnapshotRepository;

@Deprecated
@Service
class QuotePassiveProjectionService {
	private static final Logger LOGGER = LoggerFactory.getLogger(QuotePassiveProjectionService.class);
	private static final String POSITION_CHECKPOINT_ID = "quote";

	@Autowired
	private EventStoreDBProvider provider;

	@Autowired
	private PositionCheckpointService posSvc;

	@Autowired
	private QuoteEventsHelper helper;

	@Autowired
	private QuoteSnapshotRepository snapshotRepo;

	@Autowired
	private ObjectMapper mapper;

	private void handleEvent(ResolvedEvent resolvedEvent) throws StreamReadException, DatabindException, IOException {
		var event = resolvedEvent.getEvent();
		var position = event.getPosition();
		
		LOGGER.debug("Received event {} in commit {} prepare {}", event.getEventId(), position.getCommitUnsigned(), position.getPrepareUnsigned());

		try {
			var eventClass = helper.getEventClassFromType(event.getEventType());
			if (eventClass == null) {
				LOGGER.warn("No event class mapped to event type {}!", event.getEventType());
				return;
			}

			var eventData = mapper.readValue(event.getEventData(), eventClass);
			var snapshot = snapshotRepo.get(eventData.getQuoteId());

			var revision = event.getStreamRevision().getValueUnsigned();
			if (snapshot != null && revision < snapshot.getSecond()) {
				LOGGER.debug("Skipped processing commit {}, prepare {}: snapshot is already updated", position.getCommitUnsigned(),
						position.getPrepareUnsigned());
				return;
			}

			var reduced = helper.reduceEvent(snapshot == null ? null : snapshot.getFirst(), eventData);
			snapshotRepo.save(reduced, revision);
			LOGGER.debug("Finished processing commit {}, prepare {}", position.getCommitUnsigned(),
					position.getPrepareUnsigned());
		} finally {
			posSvc.setPosition(POSITION_CHECKPOINT_ID, position);
		}
	}

	@EventListener(ApplicationStartedEvent.class)
	private void onStart() throws InterruptedException, ExecutionException {
		SubscribeToAllOptions options = SubscribeToAllOptions.get();
		var checkpoint = posSvc.getPosition(POSITION_CHECKPOINT_ID);
		if (checkpoint != null) {
			options.fromPosition(checkpoint);
			LOGGER.debug("Starting catch-up from commit {}, prepare {}", checkpoint.getCommitUnsigned(),
					checkpoint.getPrepareUnsigned());
		} else {
			LOGGER.debug("Starting catch-up from the beginning");
		}

		SubscriptionFilter filter = SubscriptionFilter.newBuilder().withEventTypePrefix("QUOTE_").build();
		options.filter(filter);

		provider.getClient().subscribeToAll(new SubscriptionListener() {
			@Override
			public void onEvent(Subscription subscription, ResolvedEvent event) {
				try {
					handleEvent(event);
				} catch (Exception e) {
					LOGGER.error("Error on during event handling", e);
				}
			}

			@Override
			public void onError(Subscription subscription, Throwable throwable) {
				LOGGER.error("Error in subscription", throwable);
			}
		}, options);
	}
}
