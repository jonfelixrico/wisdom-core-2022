package com.wisdom.quote.projection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;

import com.wisdom.eventstoredb.EventStoreDBProvider;

class QuotePassiveProjectionService {
	@Autowired
	private EventStoreDBProvider esdb;
	
	@EventListener
	private void onApplicationStart (ContextStartedEvent event) {
	}
}
