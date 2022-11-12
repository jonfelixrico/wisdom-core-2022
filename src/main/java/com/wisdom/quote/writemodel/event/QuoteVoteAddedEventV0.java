package com.wisdom.quote.writemodel.event;

import java.time.Instant;

public class QuoteVoteAddedEventV0 extends BaseQuoteVoteAddedEvent {
  /**
   * Negative value for a downvote, positive value for an upvote.
   * This only existed for the legacy version of the app (app v2).
   */
  private Integer value;

  public QuoteVoteAddedEventV0(String quoteId, String userId, Instant timestamp, Integer value) {
    super(quoteId, userId, timestamp);
    this.value = value;
  }

  public static final String EVENT_TYPE = "QUOTE_VOTE_ADDED.V0";

  @Override
  public String getEventType() {
    return EVENT_TYPE;
  }

  public Integer getValue() {
    return value;
  }

}
