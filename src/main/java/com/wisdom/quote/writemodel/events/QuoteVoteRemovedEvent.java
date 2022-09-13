package com.wisdom.quote.writemodel.events;

import java.time.Instant;

public class QuoteVoteRemovedEvent extends BaseQuoteEvent {
	public static final String EVENT_TYPE = "QUOTE_VOTE_REMOVED";

	private String quoteId;
	private String userId;
	private Instant timestamp;

	public QuoteVoteRemovedEvent(String quoteId, String userId, Instant timestamp) {
		this.quoteId = quoteId;
		this.userId = userId;
		this.timestamp = timestamp;
	}

	public String getQuoteId() {
		return quoteId;
	}

	public String getUserId() {
		return userId;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	@Override
	public String getEventType() {
		return EVENT_TYPE;
	}

}
