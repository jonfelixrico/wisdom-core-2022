package com.wisdom.quote.aggregate.events;

import java.time.Instant;

import com.wisdom.eventsourcing.Event;

public class QuoteAcceptedBySystemEvent implements Event {
	private Instant timestamp;
	
	@Override
	public String getEventType() {
		return "QUOTE_ACCEPTED_BY_SYSTEM";
	}
	
}
