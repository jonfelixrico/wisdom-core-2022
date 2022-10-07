package com.wisdom.quote.writemodel.event;

import java.time.Instant;

public class QuoteSubmittedEvent extends BaseQuoteEvent {
	public static final String EVENT_TYPE = "QUOTE_SUBMITTED";

	private String quoteId;

	private String content;
	private String authorId;
	private String submitterId;

	private Instant timestamp;
	private Instant expirationDt;

	private String serverId;
	private String channelId;
	private String messageId;

	private Integer requiredVoteCount;

	public QuoteSubmittedEvent(String quoteId, String content, String authorId, String submitterId, Instant timestamp,
			Instant expirationDt, String serverId, String channelId, String messageId, Integer requiredVoteCount) {
		this.quoteId = quoteId;
		this.content = content;
		this.authorId = authorId;
		this.submitterId = submitterId;
		this.timestamp = timestamp;
		this.expirationDt = expirationDt;
		this.serverId = serverId;
		this.channelId = channelId;
		this.messageId = messageId;
		this.requiredVoteCount = requiredVoteCount;
	}

	@Override
	public String getEventType() {
		// TODO Auto-generated method stub
		return EVENT_TYPE;
	}

	@Override
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

	public Instant getTimestamp() {
		return timestamp;
	}

	public Instant getExpirationDt() {
		return expirationDt;
	}

	public String getServerId() {
		return serverId;
	}

	public String getChannelId() {
		return channelId;
	}

	public String getMessageId() {
		return messageId;
	}

	public Integer getRequiredVoteCount() {
		return requiredVoteCount;
	}

}
