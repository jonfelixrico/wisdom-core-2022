package com.wisdom.quote.writemodel.event;

import java.time.Instant;

abstract class BaseQuoteVoteAddedEvent extends BaseQuoteEvent {
  private String quoteId;
  private String userId;
  private Instant timestamp;
  
  @Override
  public String getQuoteId() {
    return quoteId;
  }

  public BaseQuoteVoteAddedEvent(String quoteId, String userId, Instant timestamp) {
    this.quoteId = quoteId;
    this.userId = userId;
    this.timestamp = timestamp;
  }

  public String getUserId() {
    return userId;
  }

  public Instant getTimestamp() {
    return timestamp;
  }
}
