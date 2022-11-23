package com.wisdom.quote.eventsourcing;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import com.wisdom.quote.entity.QuoteEntity;
import com.wisdom.quote.entity.Receive;
import com.wisdom.quote.entity.StatusDeclaration;

public class QuoteAggregate extends QuoteEntity {
  private Long revision;

  public QuoteAggregate(String id, String content, String authorId, String submitterId, Instant submitDt,
      Instant expirationDt, String serverId, String channelId, String messageId, List<Receive> receives,
      StatusDeclaration statusDeclaration, Map<String, Instant> votes, Integer requiredVoteCount, Boolean isLegacy,
      Long revision) {
    super(id, content, authorId, submitterId, submitDt, expirationDt, serverId, channelId, messageId, receives,
        statusDeclaration, votes, requiredVoteCount, isLegacy);
    this.revision = revision;
  }

  public QuoteAggregate(QuoteAggregate toClone) {
    super(toClone);
    this.revision = toClone.getRevision();
  }

  public Long getRevision() {
    return revision;
  }

  void setRevision(Long revision) {
    this.revision = revision;
  }

}
