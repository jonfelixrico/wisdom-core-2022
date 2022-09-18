package com.wisdom.quote.projection.snapshot;

import org.springframework.data.mongodb.core.mapping.Document;

import com.wisdom.quote.projection.QuoteProjectionModel;

@Document("quotes")
class QuoteMongoModel extends QuoteProjectionModel {
	private Long revision;

	private QuoteMongoModel(QuoteProjectionModel base) {
		super(base.getId(), base.getContent(), base.getAuthorId(), base.getSubmitterId(), base.getSubmitDt(),
				base.getExpirationDt(), base.getServerId(), base.getChannelId(), base.getMessageId(),
				base.getVoterIds(), base.getReceives(), base.getVerdict(), base.getRequiredVoteCount());
	}

	public QuoteMongoModel(QuoteProjectionModel base, long revision) {
		this(base);
		this.revision = revision;
	}

	public Long getRevision() {
		return revision;
	}

	public void setRevision(Long revision) {
		this.revision = revision;
	}

}
