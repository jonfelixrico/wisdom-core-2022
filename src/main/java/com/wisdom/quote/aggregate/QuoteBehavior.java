package com.wisdom.quote.aggregate;

import java.time.Instant;
import java.util.List;

public abstract class QuoteBehavior {
	private QuoteEntity entity;

	public QuoteBehavior(QuoteEntity entity) {
		this.entity = entity;
	}

	QuoteEntity getEntity() {
		return entity;
	}
	
	public void setVotes(List<String> voterIds, Instant timestamp) {
		if (entity.getVerdict() != null) {
			throw new IllegalStateException("This quote is no longer in its voting phase.");
		}

		entity.setVotingSession(new VotingSession(timestamp, voterIds));
	}

	public void receive(Receive receive) {
		if (entity.getVerdict() == null || entity.getVerdict().getStatus() != VerdictStatus.APPROVED) {
			throw new IllegalStateException("Quote does not accept receives.");
		}

		var clone = List.copyOf(entity.getReceives());
		clone.add(receive);
	}

	public void approve(Instant timestamp) {
		if (entity.getVerdict() != null) {
			throw new IllegalStateException("Quote can no longer be approved.");
		}

		entity.setVerdict(new Verdict(VerdictStatus.APPROVED, timestamp));
	}

	public void flagAsExpired(Instant timestamp) {
		if (entity.getVerdict() != null) {
			throw new IllegalStateException("Quote can no longer be flagged as expired.");
		}

		if (timestamp.isBefore(entity.getExpirationDt())) {
			throw new IllegalStateException("Provided expiration date is earlier than quote expiration date.");
		}

		entity.setVerdict(new Verdict(VerdictStatus.EXPIRED, timestamp));
	}

}
