package com.wisdom.quote.writemodel.events;

import java.time.Instant;

import com.wisdom.quote.aggregate.VoteType;

public class QuoteVoteAddedEvent extends BaseQuoteEvent {
	public static final String EVENT_TYPE = "QUOTE_VOTE_ADDED";

	private String quoteId;

	private String userId;
	private VoteType type;

	private Instant timestamp;

	public QuoteVoteAddedEvent(String quoteId, String userId, VoteType type, Instant timestamp) {
		this.quoteId = quoteId;
		this.userId = userId;
		this.type = type;
		this.timestamp = timestamp;
	}

	public String getQuoteId() {
		return quoteId;
	}

	public String getUserId() {
		return userId;
	}

	public VoteType getType() {
		return type;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	@Override
	public String getEventType() {
		return EVENT_TYPE;
	}

}
