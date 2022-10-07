package com.wisdom.quote.writemodel.events;

import java.time.Instant;

@Deprecated
public class QuoteFlaggedAsExpiredBySystemEvent extends BaseQuoteEvent {
	public static final String EVENT_TYPE = "QUOTE_FLAGGED_AS_EXPIRED_BY_SYSTEM";

	private String quoteId;
	private Instant timestamp;

	public QuoteFlaggedAsExpiredBySystemEvent(String quoteId, Instant timestamp) {
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
