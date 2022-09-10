package com.wisdom.quote.aggregate.events;

import java.time.Instant;

import com.wisdom.eventsourcing.Event;

public class QuoteFlaggedAsExpiredBySystemEvent implements Event {
	private Instant timestamp;

	public QuoteFlaggedAsExpiredBySystemEvent(Instant timestamp) {
		this.timestamp = timestamp;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	@Override
	public String getEventType() {
		return "QUOTE_FLAGGED_AS_EXPIRED_BY_SYSTEM";
	}

}
