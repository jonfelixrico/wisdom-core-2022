package com.wisdom.quote.entity;

import java.time.Instant;

public class StatusDeclaration {
	private Status status;
	private Instant timestamp;

	public StatusDeclaration(Status type, Instant timestamp) {
		this.status = type;
		this.timestamp = timestamp;
	}

	public Status getStatus() {
		return status;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

}
