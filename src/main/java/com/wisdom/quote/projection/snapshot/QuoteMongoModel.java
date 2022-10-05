package com.wisdom.quote.projection.snapshot;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import com.wisdom.quote.aggregate.Receive;
import com.wisdom.quote.aggregate.Verdict;
import com.wisdom.quote.projection.QuoteProjectionModel;

@Document("quotes")
public class QuoteMongoModel extends QuoteProjectionModel {
	private Long revision;

	@PersistenceCreator
	private QuoteMongoModel(String id, String content, String authorId, String submitterId, Instant submitDt,
			Instant expirationDt, String serverId, String channelId, String messageId, List<String> voterIds,
			List<Receive> receives, Verdict verdict, Integer requiredVoteCount, Long revision) {
		super(id, content, authorId, submitterId, submitDt, expirationDt, serverId, channelId, messageId, voterIds,
				receives, verdict, requiredVoteCount);
		this.revision = revision;
	}

	QuoteMongoModel(QuoteProjectionModel base, long revision) {
		this(base.getId(), base.getContent(), base.getAuthorId(), base.getSubmitterId(), base.getSubmitDt(),
				base.getExpirationDt(), base.getServerId(), base.getChannelId(), base.getMessageId(),
				base.getVoterIds(), base.getReceives(), base.getVerdict(), base.getRequiredVoteCount(), revision);
	}

	public Long getRevision() {
		return revision;
	}

	public void setRevision(Long revision) {
		this.revision = revision;
	}

}
