package com.wisdom.quote.writemodel.events;

import java.time.Instant;

import com.wisdom.eventsourcing.Event;
import com.wisdom.quote.aggregate.VoteType;

public class QuoteVoteAddedEvent implements Event {
	public static final String EVENT_TYPE = "QUOTE_VOTE_ADDED";

	private String quoteId;

	private String userId;
	private VoteType type;

	private Instant createDt;

	public QuoteVoteAddedEvent(String quoteId, String userId, VoteType type, Instant createDt) {
		this.quoteId = quoteId;
		this.userId = userId;
		this.type = type;
		this.createDt = createDt;
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

	public Instant getCreateDt() {
		return createDt;
	}

	@Override
	public String getEventType() {
		return EVENT_TYPE;
	}

}
