package com.wisdom.quote.aggregates;

import java.time.Instant;
import java.util.List;

public class QuoteAggregate {
	private String quoteId;
	private String content;
	
	private String authorId;
	private String submitterId;
	
	private Instant submitDt;
	
	private List<ReceiveSubAggregate> receives;

	public QuoteAggregate(String quoteId, String content, String authorId, String submitterId, Instant submitDt,
			List<ReceiveSubAggregate> receives) {
		this.quoteId = quoteId;
		this.content = content;
		this.authorId = authorId;
		this.submitterId = submitterId;
		this.submitDt = submitDt;
		this.receives = receives;
	}

	public String getQuoteId() {
		return quoteId;
	}

	public String getContent() {
		return content;
	}

	public String getAuthorId() {
		return authorId;
	}

	public String getSubmitterId() {
		return submitterId;
	}

	public Instant getSubmitDt() {
		return submitDt;
	}

	public List<ReceiveSubAggregate> getReceives() {
		return receives;
	}
	
	/**
	 * Adds a receive to the quote.
	 * @param receiveObj
	 */
	public void addReceive(ReceiveSubAggregate receiveObj) {
		// TODO dupe checking of receive id
		this.receives.add(receiveObj);
	}
}
