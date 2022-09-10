package com.wisdom.quote.aggregate.events;

import java.time.Instant;

import com.wisdom.eventsourcing.Event;

public class QuoteApprovedBySystemEvent implements Event {
	private String quoteId;
	private Instant timestamp;

	public QuoteApprovedBySystemEvent(String quoteId, Instant timestamp) {
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
		return "QUOTE_APPROVED_BY_SYSTEM";
	}

}
