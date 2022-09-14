package com.wisdom.quote.mongodb;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.wisdom.quote.aggregate.Verdict;
import com.wisdom.quote.projection.Receive;
import com.wisdom.quote.projection.Vote;

@Document("quotes")
public class QuoteMongoEntity {
	@Id
	private String id;

	private String content;
	private String authorId;
	private String submitterId;

	private Instant submitDt;
	private Instant expirationDt;

	private String serverId;
	private String channelId;
	private String messageId;

	private List<Vote> votes;
	private List<Receive> receives;
	private Verdict verdict;

	public QuoteMongoEntity(String id, String content, String authorId, String submitterId, Instant submitDt,
			Instant expirationDt, String serverId, String channelId, String messageId, List<Vote> votes,
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
		this.votes = votes;
		this.receives = receives;
		this.verdict = verdict;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	public String getSubmitterId() {
		return submitterId;
	}

	public void setSubmitterId(String submitterId) {
		this.submitterId = submitterId;
	}

	public Instant getSubmitDt() {
		return submitDt;
	}

	public void setSubmitDt(Instant submitDt) {
		this.submitDt = submitDt;
	}

	public Instant getExpirationDt() {
		return expirationDt;
	}

	public void setExpirationDt(Instant expirationDt) {
		this.expirationDt = expirationDt;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public List<Vote> getVotes() {
		return votes;
	}

	public void setVotes(List<Vote> votes) {
		this.votes = votes;
	}

	public List<Receive> getReceives() {
		return receives;
	}

	public void setReceives(List<Receive> receives) {
		this.receives = receives;
	}

	public Verdict getVerdict() {
		return verdict;
	}

	public void setVerdict(Verdict verdict) {
		this.verdict = verdict;
	}

}
