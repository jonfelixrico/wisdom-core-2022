package com.wisdom.quote.projection.passive;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;

import com.eventstore.dbclient.ReadAllOptions;
import com.wisdom.eventstoredb.EventStoreDBProvider;
import com.wisdom.eventstoredb.checkpoint.PositionCheckpointService;

class QuotePassiveProjectionService {
	private static final String POSITION_CHECKPOINT_ID = "quote";

	@Autowired
	private EventStoreDBProvider provider;

	@Autowired
	private PositionCheckpointService posSvc;

	@Autowired
	private QuotePassiveProjectionSubscriber subscriber;

	@EventListener
	private void onStart(ApplicationStartedEvent event) throws InterruptedException, ExecutionException {
		ReadAllOptions options = ReadAllOptions.get();
		options.fromPosition(posSvc.getPosition(POSITION_CHECKPOINT_ID));

		provider.getClient().readAllReactive(options).subscribe(subscriber);
	}
}
