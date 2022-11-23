package com.wisdom.quote.eventsourcing.events;

import java.time.Instant;

public class QuoteReceivedEventV1 extends QuoteReceivedEventV0 {
	public static final String EVENT_TYPE = "QUOTE_RECEIVED.V1";

	private String channelId;
	private String messageId;

	public QuoteReceivedEventV1(String quoteId, String receiveId, String userId, Instant timestamp, String serverId,
			String channelId, String messageId) {
	    super(quoteId, receiveId, userId, timestamp, serverId);
		this.channelId = channelId;
		this.messageId = messageId;
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
