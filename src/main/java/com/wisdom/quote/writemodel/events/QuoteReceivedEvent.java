package com.wisdom.quote.writemodel.events;

import java.time.Instant;

import com.wisdom.eventsourcing.Event;

public class QuoteReceivedEvent implements Event {
	public static final String EVENT_TYPE = "QUOTE_RECEIVED";

	private String quoteId;
	private String receiveId;

	private String userId;
	private Instant timestamp;

	private String serverId;
	private String channelId;
	private String messageId;

	public QuoteReceivedEvent(String quoteId, String receiveId, String userId, Instant timestamp, String serverId,
			String channelId, String messageId) {
		this.quoteId = quoteId;
		this.receiveId = receiveId;
		this.userId = userId;
		this.timestamp = timestamp;
		this.serverId = serverId;
		this.channelId = channelId;
		this.messageId = messageId;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public String getQuoteId() {
		return quoteId;
	}

	public String getReceiveId() {
		return receiveId;
	}

	public String getUserId() {
		return userId;
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

	@Override
	public String getEventType() {
		return EVENT_TYPE;
	}

}
