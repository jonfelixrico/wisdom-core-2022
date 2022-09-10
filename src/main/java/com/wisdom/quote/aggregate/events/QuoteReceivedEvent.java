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
