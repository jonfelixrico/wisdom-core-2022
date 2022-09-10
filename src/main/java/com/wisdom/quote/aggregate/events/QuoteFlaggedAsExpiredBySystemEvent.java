package com.wisdom.quote.aggregate.events;

import java.time.Instant;

import com.wisdom.eventsourcing.Event;

public class QuoteFlaggedAsExpiredBySystemEvent implements Event {
	private String quoteId;
	private Instant timestamp;

	public QuoteFlaggedAsExpiredBySystemEvent(String quoteId, Instant timestamp) {
		this.quoteId = quoteId;
		this.timestamp = timestamp;
	}

	public String getQuoteId() {
		return quoteId;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	@Override
	public String getEventType() {
		return "QUOTE_FLAGGED_AS_EXPIRED_BY_SYSTEM";
	}

}
