package com.wisdom.quote.writemodel.event;

import java.time.Instant;

public class QuoteSubmittedEventV1 extends QuoteSubmittedEventV0 {
  public static final String EVENT_TYPE = "QUOTE_SUBMITTED.V1";

  private String channelId;
  private String messageId;

  public QuoteSubmittedEventV1(String quoteId, String content, String authorId, String submitterId, Instant timestamp,
      Instant expirationDt, String serverId, String channelId, String messageId, Integer requiredVoteCount) {
    super(quoteId, content, authorId, submitterId, timestamp, expirationDt, messageId, requiredVoteCount);
    this.channelId = channelId;
    this.messageId = messageId;
  }

  @Override
  public String getEventType() {
    // TODO Auto-generated method stub
    return EVENT_TYPE;
  }

  public String getChannelId() {
    return channelId;
  }

  public String getMessageId() {
    return messageId;
  }
}
