package com.wisdom.quote.eventsourcing;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wisdom.quote.entity.Receive;
import com.wisdom.quote.entity.StatusDeclaration;

class MutableQuoteReducerModel extends QuoteAggregate {

  public MutableQuoteReducerModel(String id, String content, String authorId, String submitterId, Instant submitDt,
      Instant expirationDt, String serverId, String channelId, String messageId, List<Receive> receives,
      StatusDeclaration statusDeclaration, Map<String, Instant> votes, Integer requiredVoteCount, Boolean isLegacy,
      Long revision) {
    super(id, content, authorId, submitterId, submitDt, expirationDt, serverId, channelId, messageId, receives,
        statusDeclaration, votes, requiredVoteCount, isLegacy, revision);
  }

  public static MutableQuoteReducerModel clone(QuoteAggregate source) {
    return new MutableQuoteReducerModel(source.getId(), source.getContent(), source.getAuthorId(),
        source.getSubmitterId(),
        source.getSubmitDt(), source.getExpirationDt(), source.getServerId(), source.getChannelId(),
        source.getMessageId(),
        new ArrayList<>(source.getReceives()), source.getStatusDeclaration(), new HashMap<>(source.getVotes()),
        source.getRequiredVoteCount(), source.getIsLegacy(), source.getRevision());
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

  @Override
  public void setRevision(Long revision) {
    // TODO Auto-generated method stub
    super.setRevision(revision);
  }

}
