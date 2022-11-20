package com.wisdom.quote.readmodel;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import com.wisdom.quote.entity.QuoteEntity;
import com.wisdom.quote.entity.Receive;
import com.wisdom.quote.entity.StatusDeclaration;

public class QuoteReadModel extends QuoteEntity {
  protected Long revision;

  private QuoteReadModel(String id, String content, String authorId, String submitterId, Instant submitDt,
      Instant expirationDt, String serverId, String channelId, String messageId, List<Receive> receives,
      StatusDeclaration statusDeclaration, Map<String, Instant> votes, Integer requiredVoteCount, Boolean isLegacy,
      Long revision) {
    super(id, content, authorId, submitterId, submitDt, expirationDt, serverId, channelId, messageId, receives,
        statusDeclaration, votes, requiredVoteCount, isLegacy);
    this.revision = revision;
  }

  public long getRevision() {
    return revision;
  }

  protected static QuoteReadModel dbToDto(QuoteReadMDB db) {
    return new QuoteReadModel(
        db.getId(), db.getContent(), db.getAuthorId(), db.getSubmitterId(), db.getSubmitDt(), db.getExpirationDt(),
        db.getServerId(), db.getChannelId(), db.getMessageId(), db.getReceives(), db.getStatusDeclaration(),
        db.getVotes(), db.getRequiredVoteCount(), db.getIsLegacy(), db.getRevision());
  }
}
