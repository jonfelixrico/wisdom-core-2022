package com.wisdom.quote.eventsourcing;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eventstore.dbclient.RecordedEvent;
import com.wisdom.quote.entity.QuoteEntity;
import com.wisdom.quote.entity.Receive;
import com.wisdom.quote.entity.StatusDeclaration;

public class QuoteReducerModel extends QuoteEntity {

  private List<RecordedEvent> events;

  public QuoteReducerModel(String id, String content, String authorId, String submitterId, Instant submitDt,
      Instant expirationDt, String serverId, String channelId, String messageId, List<Receive> receives,
      StatusDeclaration statusDeclaration, Map<String, Instant> votes, Integer requiredVoteCount, Boolean isLegacy,
      List<RecordedEvent> events) {
    super(id, content, authorId, submitterId, submitDt, expirationDt, serverId, channelId, messageId, receives,
        statusDeclaration, votes, requiredVoteCount, isLegacy);
    this.events = events;
  }

  public static QuoteReducerModel clone(QuoteReducerModel source) {
    return new QuoteReducerModel(source.getId(), source.getContent(), source.getAuthorId(), source.getSubmitterId(),
        source.getSubmitDt(), source.getExpirationDt(), source.getServerId(), source.getChannelId(),
        source.getMessageId(),
        new ArrayList<>(source.getReceives()), source.getStatusDeclaration(), new HashMap<>(source.getVotes()),
        source.getRequiredVoteCount(), source.getIsLegacy(), new ArrayList<>(source.getEvents()));
  }

  public List<RecordedEvent> getEvents() {
    return events;
  }

  public void pushEvent(RecordedEvent event) {
    events.add(event);
  }

  public long getRevision() {
    var size = events.size();

    if (size == 0) {
      return -1L;
    }

    return events.get(size - 1).getStreamRevision().getValueUnsigned();
  }

  @Override
  public void setId(String id) {
    // TODO Auto-generated method stub
    super.setId(id);
  }

  @Override
  public void setContent(String content) {
    // TODO Auto-generated method stub
    super.setContent(content);
  }

  @Override
  public void setAuthorId(String authorId) {
    // TODO Auto-generated method stub
    super.setAuthorId(authorId);
  }

  @Override
  public void setSubmitterId(String submitterId) {
    // TODO Auto-generated method stub
    super.setSubmitterId(submitterId);
  }

  @Override
  public void setSubmitDt(Instant submitDt) {
    // TODO Auto-generated method stub
    super.setSubmitDt(submitDt);
  }

  @Override
  public void setExpirationDt(Instant expirationDt) {
    // TODO Auto-generated method stub
    super.setExpirationDt(expirationDt);
  }

  @Override
  public void setServerId(String serverId) {
    // TODO Auto-generated method stub
    super.setServerId(serverId);
  }

  @Override
  public void setChannelId(String channelId) {
    // TODO Auto-generated method stub
    super.setChannelId(channelId);
  }

  @Override
  public void setMessageId(String messageId) {
    // TODO Auto-generated method stub
    super.setMessageId(messageId);
  }

  @Override
  public void setReceives(List<Receive> receives) {
    // TODO Auto-generated method stub
    super.setReceives(receives);
  }

  @Override
  public void setRequiredVoteCount(Integer requiredVoteCount) {
    // TODO Auto-generated method stub
    super.setRequiredVoteCount(requiredVoteCount);
  }

  @Override
  public void setStatusDeclaration(StatusDeclaration status) {
    // TODO Auto-generated method stub
    super.setStatusDeclaration(status);
  }

  @Override
  public void setVotes(Map<String, Instant> votes) {
    // TODO Auto-generated method stub
    super.setVotes(votes);
  }

  @Override
  public void setIsLegacy(Boolean isLegacy) {
    // TODO Auto-generated method stub
    super.setIsLegacy(isLegacy);
  }
}
