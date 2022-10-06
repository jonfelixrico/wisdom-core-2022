package com.wisdom.quote.entity;

import java.time.Instant;
import java.util.List;

public abstract class QuoteBehaviorV2 extends QuoteEntity {

	protected QuoteBehaviorV2(QuoteEntity entity) {
		super(entity.getId(), entity.getContent(), entity.getAuthorId(), entity.getSubmitterId(), entity.getSubmitDt(),
				entity.getExpirationDt(), entity.getServerId(), entity.getChannelId(), entity.getMessageId(),
				entity.getReceives(), entity.getVerdict(), entity.getVotingSession(), entity.getRequiredVoteCount());
	}

	protected void updateVotingSession(VotingSession votingSession) {
		if (getVerdict() != null) {
			throw new IllegalStateException("This quote is no longer in its voting phase.");
		}

		setVotingSession(votingSession);
	}

	protected void receive(Receive receive) {
		if (getVerdict() == null || getVerdict().getStatus() != VerdictStatus.APPROVED) {
			throw new IllegalStateException("Quote does not accept receives.");
		}

		var clone = List.copyOf(getReceives());
		clone.add(receive);
	}

	protected void approve(Instant timestamp) {
		if (getVerdict() != null) {
			throw new IllegalStateException("Quote can no longer be approved.");
		}

		setVerdict(new Verdict(VerdictStatus.APPROVED, timestamp));
	}

	protected void flagAsExpired(Instant timestamp) {
		if (getVerdict() != null) {
			throw new IllegalStateException("Quote can no longer be flagged as expired.");
		}

		if (timestamp.isBefore(getExpirationDt())) {
			throw new IllegalStateException("Provided expiration date is earlier than quote expiration date.");
		}

		setVerdict(new Verdict(VerdictStatus.EXPIRED, timestamp));
	}

}
