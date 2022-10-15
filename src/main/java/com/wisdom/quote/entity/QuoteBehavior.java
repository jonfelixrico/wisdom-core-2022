package com.wisdom.quote.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class QuoteBehavior extends QuoteEntity {

  protected QuoteBehavior(String id, String content, String authorId, String submitterId, Instant submitDt,
      Instant expirationDt, String serverId, String channelId, String messageId, List<Receive> receives,
      StatusDeclaration statusDeclaration, Map<String, Instant> votes, Integer requiredVoteCount) {
    super(id, content, authorId, submitterId, submitDt, expirationDt, serverId, channelId, messageId, receives,
        statusDeclaration, votes, requiredVoteCount);
    // TODO Auto-generated constructor stub
  }

  protected QuoteBehavior(QuoteEntity entity) {
    this(entity.getId(), entity.getContent(), entity.getAuthorId(), entity.getSubmitterId(), entity.getSubmitDt(),
        entity.getExpirationDt(),
        entity.getServerId(), entity.getChannelId(), entity.getMessageId(), entity.getReceives(),
        entity.getStatusDeclaration(), entity.getVotes(), entity.getRequiredVoteCount());
  }

  protected void receive(Receive receive) {
    if (getStatusDeclaration() == null || getStatusDeclaration().getStatus() != Status.APPROVED) {
      throw new IllegalStateException("Quote does not accept receives.");
    }

    var clone = new ArrayList<>(getReceives());
    clone.add(receive);
    setReceives(clone);
  }

  protected void declareStatus(StatusDeclaration declaration) {
    if (getStatusDeclaration() != null) {
      throw new IllegalStateException("Quote already has a status.");
    }

    setStatusDeclaration(declaration);
  }

  protected void addVote(String userId, Instant timestamp) {
    Map<String, Instant> nullSafeMap = getVotes() == null ? Map.of() : getVotes();
    if (nullSafeMap.containsKey(userId)) {
      throw new IllegalStateException("User has already voted.");
    }

    var clone = new HashMap<>(nullSafeMap);
    clone.put(userId, timestamp);

    setVotes(clone);
  }

  protected void removeVote(String userId) {
    Map<String, Instant> nullSafeMap = getVotes() == null ? Map.of() : getVotes();
    if (!nullSafeMap.containsKey(userId)) {
      throw new IllegalStateException("User does not have any votes.");
    }

    var clone = new HashMap<>(nullSafeMap);
    clone.remove(userId);

    setVotes(clone);
  }

}
