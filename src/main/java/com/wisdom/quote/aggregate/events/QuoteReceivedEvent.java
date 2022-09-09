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
	
	@Override
	public String getEventType() {
		return "QUOTE_RECEIVED";
	}

}
