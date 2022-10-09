package com.wisdom.quote.readmodel;

import java.time.Instant;
import java.util.List;

import com.wisdom.quote.entity.QuoteEntity;
import com.wisdom.quote.entity.Receive;
import com.wisdom.quote.entity.StatusDeclaration;
import com.wisdom.quote.entity.VotingSession;

public abstract class QuoteReadModel extends QuoteEntity {
	protected Long revision;

	public QuoteReadModel(String id, String content, String authorId, String submitterId, Instant submitDt,
			Instant expirationDt, String serverId, String channelId, String messageId, List<Receive> receives,
			StatusDeclaration statusDeclaration, VotingSession votingSession, Integer requiredVoteCount,
			Long revision) {
		super(id, content, authorId, submitterId, submitDt, expirationDt, serverId, channelId, messageId, receives,
				statusDeclaration, votingSession, requiredVoteCount);
		this.revision = revision;
	}

	public Long getRevision() {
		return revision;
	}

	protected void setRevision(Long revision) {
		this.revision = revision;
	}

}
