package com.wisdom.quote.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class QuoteEntity {
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
  private StatusDeclaration statusDeclaration;

  private Map<String, Instant> votes;
  private Integer requiredVoteCount;

  private Boolean isLegacy;

  public QuoteEntity(String id, String content, String authorId, String submitterId, Instant submitDt,
      Instant expirationDt, String serverId, String channelId, String messageId, List<Receive> receives,
      StatusDeclaration statusDeclaration, Map<String, Instant> votes, Integer requiredVoteCount, Boolean isLegacy) {
    this.id = id;
    this.content = content;
    this.authorId = authorId;
    this.submitterId = submitterId;
    this.submitDt = submitDt;
    this.expirationDt = expirationDt;
    this.serverId = serverId;
    this.channelId = channelId;
    this.messageId = messageId;
    this.receives = receives == null ? new ArrayList<>() : receives;
    this.statusDeclaration = statusDeclaration;
    this.votes = votes == null ? new HashMap<>() : votes;
    this.requiredVoteCount = requiredVoteCount;
    this.isLegacy = isLegacy;
  }

  public QuoteEntity(QuoteEntity toClone) {
    this(toClone.getId(), toClone.getContent(), toClone.getAuthorId(), toClone.getSubmitterId(), toClone.getSubmitDt(),
        toClone.getExpirationDt(),
        toClone.getServerId(), toClone.getChannelId(), toClone.getMessageId(),
        new ArrayList<>(toClone.getReceives()), toClone.getStatusDeclaration(), new HashMap<>(toClone.getVotes()),
        toClone.getRequiredVoteCount(), toClone.getIsLegacy());
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

  public Integer getRequiredVoteCount() {
    return requiredVoteCount;
  }

  protected void setId(String id) {
    this.id = id;
  }

  protected void setContent(String content) {
    this.content = content;
  }

  protected void setAuthorId(String authorId) {
    this.authorId = authorId;
  }

  protected void setSubmitterId(String submitterId) {
    this.submitterId = submitterId;
  }

  protected void setSubmitDt(Instant submitDt) {
    this.submitDt = submitDt;
  }

  protected void setExpirationDt(Instant expirationDt) {
    this.expirationDt = expirationDt;
  }

  protected void setServerId(String serverId) {
    this.serverId = serverId;
  }

  protected void setChannelId(String channelId) {
    this.channelId = channelId;
  }

  protected void setMessageId(String messageId) {
    this.messageId = messageId;
  }

  protected void setReceives(List<Receive> receives) {
    this.receives = receives;
  }

  protected void setRequiredVoteCount(Integer requiredVoteCount) {
    this.requiredVoteCount = requiredVoteCount;
  }

  public StatusDeclaration getStatusDeclaration() {
    return statusDeclaration;
  }

  protected void setStatusDeclaration(StatusDeclaration status) {
    this.statusDeclaration = status;
  }

  public Map<String, Instant> getVotes() {
    return votes;
  }

  protected void setVotes(Map<String, Instant> votes) {
    this.votes = votes;
  }

  public Boolean getIsLegacy() {
    return isLegacy;
  }

  protected void setIsLegacy(Boolean isLegacy) {
    this.isLegacy = isLegacy;
  }

}
