package com.wisdom.quote.service;

import java.time.Instant;
import java.util.List;

import com.wisdom.quote.projection.snapshot.QuoteMongoModel;

public class PendingQuoteReadModel {
	private String id;

	private String content;
	private String authorId;
	private String submitterId;

	private Instant submitDt;
	private Instant expirationDt;

	private List<String> voterIds;
	private Integer requiredVoteCount;

	PendingQuoteReadModel(String id, String content, String authorId, String submitterId, Instant submitDt,
			Instant expirationDt, List<String> voterIds, Integer requiredVoteCount) {
		this.id = id;
		this.content = content;
		this.authorId = authorId;
		this.submitterId = submitterId;
		this.submitDt = submitDt;
		this.expirationDt = expirationDt;
		this.voterIds = voterIds;
		this.requiredVoteCount = requiredVoteCount;
	}

	PendingQuoteReadModel(QuoteMongoModel dbModel) {
		this(dbModel.getId(), dbModel.getContent(), dbModel.getAuthorId(), dbModel.getSubmitterId(),
				dbModel.getSubmitDt(), dbModel.getExpirationDt(), dbModel.getVoterIds(),
				dbModel.getRequiredVoteCount());
	}

	public String getId() {
		return id;
	}

	public String getContent() {
		return content;
	}

	public String getAuthorId() {
		return authorId;
	}

	public String getSubmitterId() {
		return submitterId;
	}

	public Instant getSubmitDt() {
		return submitDt;
	}

	public Instant getExpirationDt() {
		return expirationDt;
	}

	public List<String> getVoterIds() {
		return voterIds;
	}

	public Integer getRequiredVoteCount() {
		return requiredVoteCount;
	}

}
