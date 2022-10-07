package com.wisdom.quote.entity;

import java.time.Instant;

public class Status {
	private StatusType type;
	private Instant timestamp;

	public Status(StatusType type, Instant timestamp) {
		this.type = type;
		this.timestamp = timestamp;
	}

	public StatusType getType() {
		return type;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

}
