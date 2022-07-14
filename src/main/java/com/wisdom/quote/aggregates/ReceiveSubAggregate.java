package com.wisdom.quote.aggregates;

import java.time.Instant;

public class ReceiveSubAggregate {
	private String id;
	private String receiverId;
	private Instant receiveDt;
	private String channelId;
	
	public ReceiveSubAggregate(String id, String receiverId, Instant receiveDt, String channelId) {
		this.id = id;
		this.receiverId = receiverId;
		this.receiveDt = receiveDt;
		this.channelId = channelId;
	}

	public String getId() {
		return id;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public Instant getReceiveDt() {
		return receiveDt;
	}

	public String getChannelId() {
		return channelId;
	}
}
