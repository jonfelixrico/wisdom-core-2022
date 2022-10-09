package com.wisdom.quote.entity;

import java.time.Instant;

public class StatusDeclaration {
	private Status status;
	private Instant timestamp;

	public StatusDeclaration(Status status, Instant timestamp) {
		this.status = status;
		this.timestamp = timestamp;
	}

	public Status getStatus() {
		return status;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

}
