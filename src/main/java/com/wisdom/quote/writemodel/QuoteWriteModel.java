package com.wisdom.quote.writemodel;

import java.time.Instant;

import com.eventstore.dbclient.ExpectedRevision;
import com.wisdom.eventstoredb.utils.EventAppendBuffer;
import com.wisdom.eventstoredb.utils.EventAppendService;
import com.wisdom.quote.entity.QuoteBehavior;
import com.wisdom.quote.entity.QuoteEntity;
import com.wisdom.quote.entity.Receive;
import com.wisdom.quote.entity.Status;
import com.wisdom.quote.entity.StatusDeclaration;
import com.wisdom.quote.writemodel.event.QuoteReceivedEvent;
import com.wisdom.quote.writemodel.event.QuoteStatusDeclaredEvent;
import com.wisdom.quote.writemodel.event.QuoteVoteAddedEventV1;
import com.wisdom.quote.writemodel.event.QuoteVoteRemovedEventV1;

public class QuoteWriteModel extends QuoteBehavior {
  private EventAppendBuffer buffer;
  private EventAppendService writeSvc;

  QuoteWriteModel(QuoteEntity entity, ExpectedRevision revision, EventAppendService writeSvc) {
    super(entity);
    this.buffer = new EventAppendBuffer(String.format("quote/%s", entity.getId()), revision);
    this.writeSvc = writeSvc;
  }

  public void receive(String receiveId, String receiverId, Instant receiveDt, String serverId, String channelId,
      String messageId) {
    super.receive(new Receive(receiveId, receiveDt, receiverId, serverId, channelId, messageId));
    buffer.pushEvent(
        new QuoteReceivedEvent(getId(), receiveId, receiverId, receiveDt, serverId, channelId, messageId));
  }

  public void declareStatus(Status status, Instant timestamp) {
    super.declareStatus(new StatusDeclaration(status, timestamp));
    buffer.pushEvent(new QuoteStatusDeclaredEvent(getId(), status, timestamp));
  }

  public void addVote(String userId, Instant timestamp) {
    super.addVote(userId, timestamp);
    buffer.pushEvent(new QuoteVoteAddedEventV1(getId(), userId, timestamp));
  }

  public void removeVote(String userId, Instant timestamp) {
    super.removeVote(userId);
    buffer.pushEvent(new QuoteVoteRemovedEventV1(getId(), userId, timestamp));
  }

  public void save() throws Exception {
    this.writeSvc.appendToStream(buffer);
  }

  EventAppendBuffer getBuffer() {
    return buffer;
  }

}
