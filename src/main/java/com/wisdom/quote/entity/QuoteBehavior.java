package com.wisdom.quote.entity;

import java.util.ArrayList;

public abstract class QuoteBehavior extends QuoteEntity {

	protected QuoteBehavior(QuoteEntity entity) {
		super(entity.getId(), entity.getContent(), entity.getAuthorId(), entity.getSubmitterId(), entity.getSubmitDt(),
				entity.getExpirationDt(), entity.getServerId(), entity.getChannelId(), entity.getMessageId(),
				entity.getReceives(), entity.getStatusDeclaration(), entity.getVotingSession(),
				entity.getRequiredVoteCount());
	}

	protected void updateVotingSession(VotingSession votingSession) {
		if (getStatusDeclaration() != null) {
			throw new IllegalStateException("This quote is no longer in its voting phase.");
		}

		setVotingSession(votingSession);
	}

	protected void receive(Receive receive) {
		if (getStatusDeclaration() == null || getStatusDeclaration().getStatus() != Status.APPROVED) {
			throw new IllegalStateException("Quote does not accept receives.");
		}

		var clone = new ArrayList<>(getReceives());
		clone.add(receive);
	}

	protected void declareStatus(StatusDeclaration declaration) {
		if (getStatusDeclaration() != null) {
			throw new IllegalStateException("Quote already has a status.");
		}

		setStatusDeclaration(declaration);
	}

}
