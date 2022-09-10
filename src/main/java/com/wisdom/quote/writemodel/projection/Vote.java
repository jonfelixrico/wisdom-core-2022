package com.wisdom.quote.writemodel.projection;

import java.time.Instant;

import com.wisdom.quote.aggregate.VoteType;

public class Vote {
	private String userId;
	private VoteType type;
	private Instant voteDt;

	public String getUserId() {
		return userId;
	}

	public VoteType getType() {
		return type;
	}

	public Instant getVoteDt() {
		return voteDt;
	}

	public Vote(String userId, VoteType type, Instant voteDt) {
		this.userId = userId;
		this.type = type;
		this.voteDt = voteDt;
	}
}
