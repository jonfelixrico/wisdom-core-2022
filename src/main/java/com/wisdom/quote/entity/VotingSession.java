package com.wisdom.quote.entity;

import java.time.Instant;
import java.util.List;

public class VotingSession {
	private Instant timestamp;
	private List<String> voterIds;

	public VotingSession(Instant timestamp, List<String> voterIds) {
		this.timestamp = timestamp;
		this.voterIds = voterIds;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public List<String> getVoterIds() {
		return voterIds;
	}

}
