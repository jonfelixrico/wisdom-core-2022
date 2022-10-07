package com.wisdom.quote.writemodel;

import java.time.Instant;
import java.util.List;

import com.wisdom.quote.entity.QuoteEntity;
import com.wisdom.quote.entity.Receive;
import com.wisdom.quote.entity.StatusDeclaration;
import com.wisdom.quote.entity.VotingSession;

class QuoteProjection extends QuoteEntity {
	private Long revision;

	public QuoteProjection(String id, String content, String authorId, String submitterId, Instant submitDt,
			Instant expirationDt, String serverId, String channelId, String messageId, List<Receive> receives,
			StatusDeclaration statusDeclaration, VotingSession votingSession, Integer requiredVoteCount,
			Long revision) {
		super(id, content, authorId, submitterId, submitDt, expirationDt, serverId, channelId, messageId, receives,
				statusDeclaration, votingSession, requiredVoteCount);
		this.revision = revision;
	}

	protected QuoteProjection(QuoteEntity base, long revision) {
		this(base.getId(), base.getContent(), base.getAuthorId(), base.getSubmitterId(), base.getSubmitDt(),
				base.getExpirationDt(), base.getServerId(), base.getChannelId(), base.getMessageId(),
				base.getReceives(), base.getStatusDeclaration(), base.getVotingSession(), base.getRequiredVoteCount(),
				revision);
	}

	public Long getRevision() {
		return revision;
	}

}
