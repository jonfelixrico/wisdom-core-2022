package com.wisdom.quote.writemodel;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eventstore.dbclient.ExpectedRevision;
import com.wisdom.eventstoredb.utils.EventAppendService;
import com.wisdom.quote.entity.QuoteEntity;
import com.wisdom.quote.entity.Receive;
import com.wisdom.quote.entity.StatusDeclaration;
import com.wisdom.quote.eventsourcing.events.QuoteSubmittedEventV1;

@Service
public class QuoteWriteModelRepository {

  @Autowired
  EventAppendService eventAppendService;

  @Autowired
  QuoteProjectionService projSvc;

  public QuoteWriteModel create(String quoteId, String content, String authorId, String submitterId,
      Instant createDt, Instant expirationDt, String serverId, String channelId, String messageId,
      int requiredVoteCount) {
    var entity = new QuoteEntityImpl(quoteId, content, authorId, submitterId, createDt, expirationDt, serverId,
        channelId, messageId, null, null, null, null, false);
    var writeModel = new QuoteWriteModel(entity, ExpectedRevision.NO_STREAM, eventAppendService);

    writeModel.getBuffer().pushEvent(new QuoteSubmittedEventV1(quoteId, content, authorId, submitterId, createDt,
        expirationDt, serverId, channelId, messageId, requiredVoteCount)); // TODO adjust the required vote
                                                                           // count
    return writeModel;

  }

  public QuoteWriteModel get(String quoteId) throws Exception {
    var result = projSvc.getProjection(quoteId);
    if (result == null) {
      return null;
    }

    return new QuoteWriteModel(result, ExpectedRevision.expectedRevision(result.getRevision()),
        eventAppendService);
  }
}

class QuoteEntityImpl extends QuoteEntity {

  public QuoteEntityImpl(String id, String content, String authorId, String submitterId, Instant submitDt,
      Instant expirationDt, String serverId, String channelId, String messageId, List<Receive> receives,
      StatusDeclaration statusDeclaration, Map<String, Instant> votes, Integer requiredVoteCount, Boolean isLegacy) {
    super(id, content, authorId, submitterId, submitDt, expirationDt, serverId, channelId, messageId, receives,
        statusDeclaration, votes, requiredVoteCount, isLegacy);
    // TODO Auto-generated constructor stub
  }

}
