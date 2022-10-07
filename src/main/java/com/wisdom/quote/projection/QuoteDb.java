package com.wisdom.quote.projection;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import com.wisdom.quote.entity.QuoteEntity;
import com.wisdom.quote.entity.Receive;
import com.wisdom.quote.entity.StatusDeclaration;
import com.wisdom.quote.entity.VotingSession;

@Document("quote-snapshot")
class QuoteDb extends QuoteProjection {
	@PersistenceCreator
	public QuoteDb(String id, String content, String authorId, String submitterId, Instant submitDt,
			Instant expirationDt, String serverId, String channelId, String messageId, List<Receive> receives,
			StatusDeclaration statusDeclaration, VotingSession votingSession, Integer requiredVoteCount,
			Long revision) {
		super(id, content, authorId, submitterId, submitDt, expirationDt, serverId, channelId, messageId, receives,
				statusDeclaration, votingSession, requiredVoteCount, revision);
	}

	QuoteDb(QuoteEntity base, long revision) {
		super(base, revision);
	}
}
