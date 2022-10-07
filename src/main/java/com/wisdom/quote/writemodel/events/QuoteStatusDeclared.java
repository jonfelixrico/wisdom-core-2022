package com.wisdom.quote.writemodel.events;

import java.time.Instant;

public class QuoteStatusDeclared extends BaseQuoteEvent {
	public static final String EVENT_TYPE = "QUOTE_STATUS_DECLARED";

	private String quoteId;
	private Instant timestamp;

	public QuoteStatusDeclared(String quoteId, Instant timestamp) {
		this.quoteId = quoteId;
		this.timestamp = timestamp;
	}

	@Override
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
