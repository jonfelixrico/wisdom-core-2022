package com.wisdom.quote.projection;

import java.time.Instant;
import java.util.List;

import com.wisdom.quote.aggregate.Receive;
import com.wisdom.quote.aggregate.Verdict;
import com.wisdom.quote.aggregate.VotingSession;
import com.wisdom.quote.entity.QuoteEntity;

public abstract class QuoteProjection extends QuoteEntity {
	private Long revision;

	public QuoteProjection(String id, String content, String authorId, String submitterId, Instant submitDt,
			Instant expirationDt, String serverId, String channelId, String messageId, List<Receive> receives,
			Verdict verdict, VotingSession votingSession, Integer requiredVoteCount, Long revision) {
		super(id, content, authorId, submitterId, submitDt, expirationDt, serverId, channelId, messageId, receives,
				verdict, votingSession, requiredVoteCount);
		this.revision = revision;
	}
	
	protected QuoteProjection(QuoteEntity base, long revision) {
		this(base.getId(), base.getContent(), base.getAuthorId(), base.getSubmitterId(), base.getSubmitDt(),
				base.getExpirationDt(), base.getServerId(), base.getChannelId(), base.getMessageId(),
				base.getReceives(), base.getVerdict(), base.getVotingSession(), base.getRequiredVoteCount(), revision);
	}

	public Long getRevision() {
		return revision;
	}

}
