package com.wisdom.quote.aggregate;

import java.time.Instant;
import java.util.List;

@Deprecated
public abstract class QuoteAggregate {
	private Instant expirationDt;
	private List<String> voterIds;
	private List<String> receiveIds;
	private Verdict verdict;

	public QuoteAggregate(Instant expirationDt, List<String> voterIds, List<String> receiveIds, Verdict verdict) {
		this.expirationDt = expirationDt;
		this.voterIds = voterIds;
		this.receiveIds = receiveIds;
		this.verdict = verdict;
	}

	public void setVotes(List<String> voterIds) {
		if (verdict != null) {
			throw new IllegalStateException("This quote is no longer in its voting phase.");
		}

		this.voterIds = voterIds;
	}

	public void receive(String receiveId) {
		if (verdict == null || verdict.getStatus() != VerdictStatus.APPROVED) {
			throw new IllegalStateException("Quote does not accept receives.");
		}

		receiveIds.add(receiveId);
	}

	public void approve(Instant acceptDt) {
		if (verdict != null) {
			throw new IllegalStateException("Quote can no longer be approved.");
		}

		verdict = new Verdict(VerdictStatus.APPROVED, acceptDt);
	}

	public void flagAsExpired(Instant expireDt) {
		if (verdict != null) {
			throw new IllegalStateException("Quote can no longer be flagged as expired.");
		}

		if (expireDt.isBefore(expirationDt)) {
			throw new IllegalStateException("Provided expiration date is earlier than quote expiration date.");
		}

		verdict = new Verdict(VerdictStatus.EXPIRED, expireDt);
	}

	public Instant getExpirationDt() {
		return expirationDt;
	}

	public List<String> getReceiveIds() {
		return List.copyOf(receiveIds);
	}

	public Verdict getVerdict() {
		return verdict;
	}
	
	public List<String> getVoterIds() {
		return voterIds;
	}
}
