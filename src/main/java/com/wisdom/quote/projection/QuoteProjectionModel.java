package com.wisdom.quote.projection;

import java.time.Instant;
import java.util.List;

import com.wisdom.quote.aggregate.Verdict;

public abstract class QuoteProjectionModel {
	protected String id;

	protected String content;
	protected String authorId;
	protected String submitterId;

	protected Instant submitDt;
	protected Instant expirationDt;

	protected String serverId;
	protected String channelId;
	protected String messageId;

	protected List<String> voterIds;
	protected List<Receive> receives;
	protected Verdict verdict;

	protected Integer requiredVoteCount;

	public QuoteProjectionModel(String id, String content, String authorId, String submitterId, Instant submitDt,
			Instant expirationDt, String serverId, String channelId, String messageId, List<String> voterIds,
			List<Receive> receives, Verdict verdict, Integer requiredVoteCount) {
		this.id = id;
		this.content = content;
		this.authorId = authorId;
		this.submitterId = submitterId;
		this.submitDt = submitDt;
		this.expirationDt = expirationDt;
		this.serverId = serverId;
		this.channelId = channelId;
		this.messageId = messageId;
		this.voterIds = voterIds;
		this.receives = receives;
		this.verdict = verdict;
		this.requiredVoteCount = requiredVoteCount;
	}

	public Integer getRequiredVoteCount() {
		return requiredVoteCount;
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

	public String getServerId() {
		return serverId;
	}

	public String getChannelId() {
		return channelId;
	}

	public String getMessageId() {
		return messageId;
	}

	public List<String> getVoterIds() {
		return voterIds;
	}

	public List<Receive> getReceives() {
		return receives;
	}

	public Verdict getVerdict() {
		return verdict;
	}
}
