package com.wisdom.quote.aggregate.events;

import java.time.Instant;

import com.wisdom.eventsourcing.Event;

public class QuoteCreatedEvent implements Event {
	private String id;

	private String content;
	private String authorId;
	private String submitterId;
	
	private Instant createDt;
	private Instant expirationDt;
	
	private String serverId;
	private String channelId;
	private String messageId;
	
	@Override
	public String getEventType() {
		// TODO Auto-generated method stub
		return "QUOTE_CREATED";
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

	public Instant getCreateDt() {
		return createDt;
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
