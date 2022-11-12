package com.wisdom.quote.entity;

import java.time.Instant;

public class Receive {
  private String id;
  private Instant timestamp;
  private String userId;

  private String serverId;
  private String channelId;
  private String messageId;

  private Boolean isLegacy;

  public String getId() {
    return id;
  }

  public Instant getTimestamp() {
    return timestamp;
  }

  public String getUserId() {
    return userId;
  }

  public String getServerId() {
    return serverId;
  }

  public String getChannelId() {
    return channelId;
  }

  public String getMessageId() {
    return messageId;
  }

  public Boolean getIsLegacy() {
    return isLegacy;
  }

  public Receive(String id, Instant timestamp, String userId, String serverId, String channelId, String messageId,
      Boolean isLegacy) {
    this.id = id;
    this.timestamp = timestamp;
    this.userId = userId;
    this.serverId = serverId;
    this.channelId = channelId;
    this.messageId = messageId;
    this.isLegacy = isLegacy;
  }

}
