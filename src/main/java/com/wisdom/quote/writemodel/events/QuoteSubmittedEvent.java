package com.wisdom.quote.writemodel.events;

import java.time.Instant;

import com.wisdom.eventsourcing.Event;

public class QuoteSubmittedEvent implements Event {
	public static final String EVENT_TYPE = "QUOTE_SUBMITTED";

	private String id;

	private String content;
	private String authorId;
	private String submitterId;

	private Instant timestamp;
	private Instant expirationDt;

	private String serverId;
	private String channelId;
	private String messageId;

	public QuoteSubmittedEvent(String id, String content, String authorId, String submitterId, Instant timestamp,
			Instant expirationDt, String serverId, String channelId, String messageId) {
		this.id = id;
		this.content = content;
		this.authorId = authorId;
		this.submitterId = submitterId;
		this.timestamp = timestamp;
		this.expirationDt = expirationDt;
		this.serverId = serverId;
		this.channelId = channelId;
		this.messageId = messageId;
	}

	@Override
	public String getEventType() {
		// TODO Auto-generated method stub
		return EVENT_TYPE;
	}

	public String getId() {
		return id;
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
}
