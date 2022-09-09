package com.wisdom.quote.aggregate.events;

import java.time.Instant;

import com.wisdom.eventsourcing.Event;
import com.wisdom.quote.aggregate.VoteType;

public class QuoteVoteAddedEvent implements Event {

	private String quoteId;

	private String userId;
	private VoteType type;

	private Instant createDt;
	
	@Override
	public String getEventType() {
		return "QUOTE_VOTE_ADDED";
	}
	
}
