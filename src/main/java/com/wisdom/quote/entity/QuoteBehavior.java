package com.wisdom.quote.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class QuoteBehavior extends QuoteEntity {
  private static Logger LOGGER = LoggerFactory.getLogger(QuoteBehavior.class);

  protected QuoteBehavior(QuoteEntity entity) {
    super(entity.getId(), entity.getContent(), entity.getAuthorId(), entity.getSubmitterId(), entity.getSubmitDt(),
        entity.getExpirationDt(),
        entity.getServerId(), entity.getChannelId(), entity.getMessageId(), entity.getReceives(),
        entity.getStatusDeclaration(), entity.getVotes(), entity.getRequiredVoteCount(), entity.getIsLegacy());
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
    if (getVotes().containsKey(userId)) {
      throw new IllegalStateException("User has already voted.");
    }

    var clone = new HashMap<>(getVotes());
    clone.put(userId, timestamp);

    setVotes(clone);
  }

  protected void removeVote(String userId) {
    if (!getVotes().containsKey(userId)) {
      LOGGER.warn("Tried to remove nonexistent vote for user {} for quote {}", userId, getId());
      return;
    }

    var clone = new HashMap<>(getVotes());
    clone.remove(userId);

    setVotes(clone);
  }

}
