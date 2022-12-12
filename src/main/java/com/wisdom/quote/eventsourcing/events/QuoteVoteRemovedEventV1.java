package com.wisdom.quote.eventsourcing.events;

import java.time.Instant;

public class QuoteVoteRemovedEventV1 extends BaseQuoteEvent {
  public static final String EVENT_TYPE = "QUOTE_VOTE_REMOVED.V1";

  private String quoteId;
  private String userId;
  private Instant timestamp;

  public QuoteVoteRemovedEventV1(String quoteId, String userId, Instant timestamp) {
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

  @Override
  public String getEventType() {
    return EVENT_TYPE;
  }

  @Override
  public String getQuoteId() {
    return quoteId;
  }

}
