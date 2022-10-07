package com.wisdom.quote.writemodel.events;

import java.time.Instant;

import com.wisdom.quote.entity.Status;

public class QuoteStatusDeclaredEvent extends BaseQuoteEvent {
	public static final String EVENT_TYPE = "QUOTE_STATUS_DECLARED";

	private String quoteId;
	private Instant timestamp;
	private Status status;

	public QuoteStatusDeclaredEvent(String quoteId, Status status, Instant timestamp) {
		this.quoteId = quoteId;
		this.timestamp = timestamp;
		this.status = status;
	}

	public String getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(String quoteId) {
		this.quoteId = quoteId;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public String getEventType() {
		return EVENT_TYPE;
	}

}
