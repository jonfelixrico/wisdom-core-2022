package com.wisdom.quote.writemodel.events;

import java.time.Instant;
import java.util.List;

public class QuoteVotesModifiedEvent extends BaseQuoteEvent {
	public static final String EVENT_TYPE = "QUOTE_VOTES_MODIFIED";

	private String quoteId;
	private List<String> voterIds;
	private Instant timestamp;

	public QuoteVotesModifiedEvent(String quoteId, List<String> voterIds, Instant timestamp) {
		this.quoteId = quoteId;
		this.voterIds = voterIds;
		this.timestamp = timestamp;
	}

	@Override
	public String getEventType() {
		return EVENT_TYPE;
	}

	@Override
	public String getQuoteId() {
		return quoteId;
	}

	public List<String> getVoterIds() {
		return voterIds;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

}
