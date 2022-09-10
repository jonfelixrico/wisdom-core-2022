package com.wisdom.quote.writemodel.events;

import java.time.Instant;

import com.wisdom.eventsourcing.Event;

public class QuoteFlaggedAsExpiredBySystemEvent implements Event {
	public static final String EVENT_TYPE = "QUOTE_FLAGGED_AS_EXPIRED_BY_SYSTEM";

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
		return EVENT_TYPE;
	}

}
