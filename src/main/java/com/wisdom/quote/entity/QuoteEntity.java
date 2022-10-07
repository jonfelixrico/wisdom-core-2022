package com.wisdom.quote.entity;

import java.time.Instant;
import java.util.List;

public class QuoteEntity {
	private String id;

	private String content;
	private String authorId;
	private String submitterId;

	private Instant submitDt;
	private Instant expirationDt;

	private String serverId;
	private String channelId;
	private String messageId;

	private List<Receive> receives;
	private Verdict verdict;
	private StatusDeclaration statusDeclaration;

	private VotingSession votingSession;
	private Integer requiredVoteCount;

	public QuoteEntity(String id, String content, String authorId, String submitterId, Instant submitDt,
			Instant expirationDt, String serverId, String channelId, String messageId, List<Receive> receives,
			StatusDeclaration status, VotingSession votingSession, Integer requiredVoteCount) {
		this.id = id;
		this.content = content;
		this.authorId = authorId;
		this.submitterId = submitterId;
		this.submitDt = submitDt;
		this.expirationDt = expirationDt;
		this.serverId = serverId;
		this.channelId = channelId;
		this.messageId = messageId;
		this.receives = receives;
		this.statusDeclaration = status;
		this.votingSession = votingSession;
		this.requiredVoteCount = requiredVoteCount;
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

	public List<Receive> getReceives() {
		return receives;
	}

	public Verdict getVerdict() {
		return verdict;
	}

	public VotingSession getVotingSession() {
		return votingSession;
	}

	public Integer getRequiredVoteCount() {
		return requiredVoteCount;
	}

	void setId(String id) {
		this.id = id;
	}

	void setContent(String content) {
		this.content = content;
	}

	void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	void setSubmitterId(String submitterId) {
		this.submitterId = submitterId;
	}

	void setSubmitDt(Instant submitDt) {
		this.submitDt = submitDt;
	}

	void setExpirationDt(Instant expirationDt) {
		this.expirationDt = expirationDt;
	}

	void setServerId(String serverId) {
		this.serverId = serverId;
	}

	void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	void setReceives(List<Receive> receives) {
		this.receives = receives;
	}

	void setVerdict(Verdict verdict) {
		this.verdict = verdict;
	}

	void setVotingSession(VotingSession votingSession) {
		this.votingSession = votingSession;
	}

	void setRequiredVoteCount(Integer requiredVoteCount) {
		this.requiredVoteCount = requiredVoteCount;
	}

	public StatusDeclaration getStatusDeclaration() {
		return statusDeclaration;
	}

	void setStatusDeclaration(StatusDeclaration status) {
		this.statusDeclaration = status;
	}
}
