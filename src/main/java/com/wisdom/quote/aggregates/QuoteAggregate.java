package com.wisdom.quote.aggregates;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public abstract class QuoteAggregate {
	private Instant expirationDt;

	private Map<String, VoteType> votes;
	private List<ReceiveInput> receives;
	
	private Verdict verdict;
	
	void addVote(String voterId, VoteType voteType, Instant voteDt) {
		if (verdict != null) {
			throw new IllegalStateException("Quote no longer accepts votes.");
		}
		
		if (voteDt.isAfter(expirationDt)) {
			throw new IllegalStateException("Quote no longer accepts votes.");
		}
		
		votes.put(voterId, voteType);
	}

	void receive(ReceiveInput receive) {
		if (verdict == null || verdict.getStatus() != VerdictStatus.ACCEPTED) {
			throw new IllegalStateException("Quote does not accept receives.");
		}
		
		receives.add(receive);
	}
	
	void cancel(Instant cancelDt) {
		if (verdict != null) {
			throw new IllegalStateException("Quote can no longer be cancelled.");
		}
		
		verdict = VerdictImpl.create(VerdictStatus.CANCELLED, cancelDt);
	}
	
	void accept(Instant acceptDt) {
		if (verdict != null) {
			throw new IllegalStateException("Quote can no longer be accepted.");
		}
		
		verdict = VerdictImpl.create(VerdictStatus.ACCEPTED, acceptDt);
	}
	
	void flagAsExpired (Instant expireDt) {
		if (verdict != null) {
			throw new IllegalStateException("Quote can no longer be flagged as expired.");
		}
		
		if (expireDt.isBefore(expirationDt)) {
			throw new IllegalStateException("Provided expiration date is earlier than quote expiration date.");
		}
		
		verdict = VerdictImpl.create(VerdictStatus.EXPIRED, expireDt);
	}
}
