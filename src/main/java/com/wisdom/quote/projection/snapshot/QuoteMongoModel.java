package com.wisdom.quote.projection.snapshot;

import java.time.Instant;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.wisdom.quote.aggregate.Verdict;
import com.wisdom.quote.projection.QuoteProjectionModel;
import com.wisdom.quote.projection.Receive;

@Document("quotes")
class QuoteMongoModel extends QuoteProjectionModel {
	public static QuoteMongoModel convertFromBase(QuoteProjectionModel base, long revision) {
		return new QuoteMongoModel(base.getId(), base.getContent(), base.getAuthorId(), base.getSubmitterId(),
				base.getSubmitDt(), base.getExpirationDt(), base.getServerId(), base.getChannelId(),
				base.getMessageId(), base.getVoterIds(), base.getReceives(), base.getVerdict(),
				base.getRequiredVoteCount(), revision);
	}

	private Long revision;

	/**
	 * Required for MongoDB reads
	 */
	QuoteMongoModel(String id, String content, String authorId, String submitterId, Instant submitDt,
			Instant expirationDt, String serverId, String channelId, String messageId, List<String> voterIds,
			List<Receive> receives, Verdict verdict, Integer requiredVoteCount, Long revision) {
		super(id, content, authorId, submitterId, submitDt, expirationDt, serverId, channelId, messageId, voterIds,
				receives, verdict, requiredVoteCount);
		this.revision = revision;
	}

	public Long getRevision() {
		return revision;
	}

	public void setRevision(Long revision) {
		this.revision = revision;
	}

}
