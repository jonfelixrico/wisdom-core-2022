package com.wisdom.quote.aggregate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuoteAggregate {
	private Instant expirationDt;

	private Map<String, VoteType> votes;
	private List<String> receiveIds;

	private Verdict verdict;

	public QuoteAggregate(Instant expirationDt, Map<String, VoteType> votes, List<String> receives, Verdict verdict) {
		this.expirationDt = expirationDt;

		this.votes = new HashMap<>();
		this.votes.putAll(votes);

		this.receiveIds = new ArrayList<>();
		this.receiveIds.addAll(receives);

		this.verdict = verdict;
	}

	public void addVote(String voterId, VoteType voteType, Instant voteDt) {
		if (verdict != null) {
			throw new IllegalStateException("Quote no longer accepts votes.");
		}

		if (voteDt.isAfter(expirationDt)) {
			throw new IllegalStateException("Quote no longer accepts votes.");
		}

		votes.put(voterId, voteType);
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

	public Map<String, VoteType> getVotes() {
		return Map.copyOf(votes);
	}

	public List<String> getReceiveIds() {
		return List.copyOf(receiveIds);
	}

	public Verdict getVerdict() {
		return verdict;
	}
}
