package com.wisdom.quote.writemodel.event;

import java.time.Instant;

/**
 * Legacy version of the quote received event. The only difference is that the
 * message and channel ids are not recorded.
 * 
 * @author Felix
 *
 */
public class QuoteReceivedEventV0 extends BaseQuoteEvent {
  public static final String EVENT_TYPE = "QUOTE_RECEIVED.V0";

  private String quoteId;
  private String receiveId;

  private String userId;
  private Instant timestamp;

  private String serverId;

  public QuoteReceivedEventV0(String quoteId, String receiveId, String userId, Instant timestamp, String serverId) {
    this.quoteId = quoteId;
    this.receiveId = receiveId;
    this.userId = userId;
    this.timestamp = timestamp;
    this.serverId = serverId;
  }

  public Instant getTimestamp() {
    return timestamp;
  }

  @Override
  public String getQuoteId() {
    return quoteId;
  }

  public String getReceiveId() {
    return receiveId;
  }

  public String getUserId() {
    return userId;
  }

  public String getServerId() {
    return serverId;
  }

  @Override
  public String getEventType() {
    return EVENT_TYPE;
  }

}
