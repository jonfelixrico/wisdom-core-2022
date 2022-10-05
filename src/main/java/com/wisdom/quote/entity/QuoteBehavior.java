package com.wisdom.quote.entity;

import java.time.Instant;
import java.util.List;

import com.wisdom.quote.aggregate.Receive;
import com.wisdom.quote.aggregate.Verdict;
import com.wisdom.quote.aggregate.VerdictStatus;
import com.wisdom.quote.aggregate.VotingSession;

public abstract class QuoteBehavior {
	private QuoteEntity entity;

	public QuoteBehavior(QuoteEntity entity) {
		this.entity = entity;
	}

	protected QuoteEntity getEntity() {
		return entity;
	}
	
	protected void updateVotingSession(VotingSession votingSession) {
		if (entity.getVerdict() != null) {
			throw new IllegalStateException("This quote is no longer in its voting phase.");
		}

		entity.setVotingSession(votingSession);
	}

	protected void receive(Receive receive) {
		if (entity.getVerdict() == null || entity.getVerdict().getStatus() != VerdictStatus.APPROVED) {
			throw new IllegalStateException("Quote does not accept receives.");
		}

		var clone = List.copyOf(entity.getReceives());
		clone.add(receive);
	}

	protected void approve(Instant timestamp) {
		if (entity.getVerdict() != null) {
			throw new IllegalStateException("Quote can no longer be approved.");
		}

		entity.setVerdict(new Verdict(VerdictStatus.APPROVED, timestamp));
	}

	protected void flagAsExpired(Instant timestamp) {
		if (entity.getVerdict() != null) {
			throw new IllegalStateException("Quote can no longer be flagged as expired.");
		}

		if (timestamp.isBefore(entity.getExpirationDt())) {
			throw new IllegalStateException("Provided expiration date is earlier than quote expiration date.");
		}

		entity.setVerdict(new Verdict(VerdictStatus.EXPIRED, timestamp));
	}

}
