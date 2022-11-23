package com.wisdom.quote.writemodel;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import com.wisdom.quote.entity.Receive;
import com.wisdom.quote.entity.StatusDeclaration;
import com.wisdom.quote.eventsourcing.QuoteReducerModel;
import com.wisdom.quote.readmodel.QuoteSnapshot;

class QuoteProjection extends QuoteSnapshot {

  QuoteProjection(String id, String content, String authorId, String submitterId, Instant submitDt,
      Instant expirationDt, String serverId, String channelId, String messageId, List<Receive> receives,
      StatusDeclaration statusDeclaration, Map<String, Instant> votes, Integer requiredVoteCount, Boolean isLegacy,
      Long revision) {
    super(id, content, authorId, submitterId, submitDt, expirationDt, serverId, channelId, messageId, receives,
        statusDeclaration, votes, requiredVoteCount, isLegacy, revision);
    // TODO Auto-generated constructor stub
  }

  QuoteProjection(QuoteReducerModel toClone) {
    super(toClone);
    // TODO Auto-generated constructor stub
  }

}
