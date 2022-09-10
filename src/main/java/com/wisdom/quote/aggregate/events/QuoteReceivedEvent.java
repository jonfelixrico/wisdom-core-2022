package com.wisdom.quote.aggregate.events;

import java.time.Instant;

import com.wisdom.eventsourcing.Event;

public class QuoteReceivedEvent implements Event {

	private String quoteId;
	private String receiveId;

	private String userId;
	private Instant createDt;

	private String serverId;
	private String channelId;
	private String messageId;

	public QuoteReceivedEvent(String quoteId, String receiveId, String userId, Instant createDt, String serverId,
			String channelId, String messageId) {
		this.quoteId = quoteId;
		this.receiveId = receiveId;
		this.userId = userId;
		this.createDt = createDt;
		this.serverId = serverId;
		this.channelId = channelId;
		this.messageId = messageId;
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

	public Instant getCreateDt() {
		return createDt;
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
		return "QUOTE_RECEIVED";
	}

}
