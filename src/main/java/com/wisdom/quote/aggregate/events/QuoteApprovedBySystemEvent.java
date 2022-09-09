package com.wisdom.quote.aggregate.events;

import java.time.Instant;

import com.wisdom.eventsourcing.Event;

public class QuoteApprovedBySystemEvent implements Event {
	private Instant timestamp;
	
	@Override
	public String getEventType() {
		return "QUOTE_APPROVED_BY_SYSTEM";
	}
	
}
