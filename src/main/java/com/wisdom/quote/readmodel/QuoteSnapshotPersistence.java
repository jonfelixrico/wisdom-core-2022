package com.wisdom.quote.readmodel;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import com.wisdom.quote.entity.Receive;
import com.wisdom.quote.entity.StatusDeclaration;
import com.wisdom.quote.eventsourcing.QuoteReducerModel;

/**
 * MongoDB object for the quote read model.
 * 
 * @author Felix
 *
 */
@Document("quote-snapshot")
class QuoteSnapshotPersistence extends QuoteSnapshot {
  @PersistenceCreator
  public QuoteSnapshotPersistence(String id, String content, String authorId, String submitterId, Instant submitDt,
      Instant expirationDt, String serverId, String channelId, String messageId, List<Receive> receives,
      StatusDeclaration statusDeclaration, Map<String, Instant> votes, Integer requiredVoteCount, Boolean isLegacy,
      Long revision) {
    super(id, content, authorId, submitterId, submitDt, expirationDt, serverId, channelId, messageId, receives,
        statusDeclaration, votes, requiredVoteCount, isLegacy, revision);
    // TODO Auto-generated constructor stub
  }

  public QuoteSnapshotPersistence(QuoteReducerModel toClone) {
    super(toClone);
    // TODO Auto-generated constructor stub
  }

}
