package com.wisdom.quote.eventsourcing.events;

import java.time.Instant;

public class QuoteVoteAddedEventV1 extends BaseQuoteVoteAddedEvent {
  public QuoteVoteAddedEventV1(String quoteId, String userId, Instant timestamp) {
    super(quoteId, userId, timestamp);
  }

  public static final String EVENT_TYPE = "QUOTE_VOTE_ADDED.V1";

  @Override
  public String getEventType() {
    return EVENT_TYPE;
  }
}
