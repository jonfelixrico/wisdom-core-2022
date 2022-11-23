package com.wisdom.quote.writemodel;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import com.wisdom.quote.entity.QuoteEntity;
import com.wisdom.quote.entity.Receive;
import com.wisdom.quote.entity.StatusDeclaration;

class QuoteEntityImpl extends QuoteEntity {

  public QuoteEntityImpl(String id, String content, String authorId, String submitterId, Instant submitDt,
      Instant expirationDt, String serverId, String channelId, String messageId, List<Receive> receives,
      StatusDeclaration statusDeclaration, Map<String, Instant> votes, Integer requiredVoteCount, Boolean isLegacy) {
    super(id, content, authorId, submitterId, submitDt, expirationDt, serverId, channelId, messageId, receives,
        statusDeclaration, votes, requiredVoteCount, isLegacy);
    // TODO Auto-generated constructor stub
  }

}
