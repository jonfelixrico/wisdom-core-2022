package com.wisdom.quote.writemodel.projection;

import java.time.Instant;
import java.util.List;
import java.util.Map;

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

	private Map<String, Vote> votes;
	private List<Receive> receives;
	private Verdict verdict;

	public QuoteProjectionModel(String id, String content, String authorId, String submitterId, Instant submitDt,
			Instant expirationDt, String serverId, String channelId, String messageId, Map<String, Vote> votes,
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
		this.votes = votes == null ? Map.of() : Map.copyOf(votes);
		this.receives = receives == null ? List.of() : List.copyOf(receives);
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

	public Map<String, Vote> getVotes() {
		return votes;
	}

	public List<Receive> getReceives() {
		return receives;
	}

	public Verdict getVerdict() {
		return verdict;
	}

}
