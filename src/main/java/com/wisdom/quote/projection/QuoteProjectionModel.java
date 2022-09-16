package com.wisdom.quote.projection;

import java.time.Instant;
import java.util.List;

import com.wisdom.quote.aggregate.Verdict;

public class QuoteProjectionModel {
	private String id;

	private String content;
	private String authorId;
	private String submitterId;

	private Instant submitDt;
	private Instant expirationDt;

	private String serverId;
	private String channelId;
	private String messageId;

	private List<String> voterIds;
	private List<Receive> receives;
	private Verdict verdict;

	public QuoteProjectionModel(String id, String content, String authorId, String submitterId, Instant submitDt,
			Instant expirationDt, String serverId, String channelId, String messageId, List<String> voterIds,
			List<Receive> receives, Verdict verdict) {
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
