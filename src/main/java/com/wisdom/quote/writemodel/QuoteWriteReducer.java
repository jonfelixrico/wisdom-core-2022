package com.wisdom.quote.writemodel;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eventstore.dbclient.RecordedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wisdom.quote.entity.QuoteEntity;
import com.wisdom.quote.entity.Receive;
import com.wisdom.quote.entity.StatusDeclaration;
import com.wisdom.quote.eventsourcing.QuoteEventsReducer;
import com.wisdom.quote.eventsourcing.QuoteReducerModel;

@Service
public class QuoteWriteReducer {

  @Autowired
  private ObjectMapper mapper;

  public QuoteEntity reduceEvent(QuoteEntity baseModel, RecordedEvent event) throws Exception {
    var reducer = new QuoteEventsReducer(mapper, quoteId -> QuoteReducerModelImpl.fromEntity(baseModel));
    return reducer.reduce(event);
  }
}

class QuoteReducerModelImpl extends QuoteReducerModel {

  public QuoteReducerModelImpl(String id, String content, String authorId, String submitterId, Instant submitDt,
      Instant expirationDt, String serverId, String channelId, String messageId, List<Receive> receives,
      StatusDeclaration statusDeclaration, Map<String, Instant> votes, Integer requiredVoteCount, Boolean isLegacy,
      Long revision) {
    super(id, content, authorId, submitterId, submitDt, expirationDt, serverId, channelId, messageId, receives,
        statusDeclaration, votes, requiredVoteCount, isLegacy, revision);
    // TODO Auto-generated constructor stub
  }

  public static QuoteReducerModelImpl fromEntity(QuoteEntity entity) {
    return new QuoteReducerModelImpl(entity.getId(), entity.getContent(), entity.getAuthorId(), entity.getSubmitterId(),
        entity.getSubmitDt(), entity.getExpirationDt(), entity.getServerId(),
        entity.getChannelId(), entity.getMessageId(), entity.getReceives(), entity.getStatusDeclaration(),
        entity.getVotes(), entity.getRequiredVoteCount(), entity.getIsLegacy(), null);
  }
}
