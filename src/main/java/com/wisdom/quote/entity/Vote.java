package com.wisdom.quote.entity;

import java.time.Instant;

public class Vote {
  private String userId;
  private Instant timestamp;

  public Vote(String userId, Instant timestamp) {
    this.userId = userId;
    this.timestamp = timestamp;
  }

  protected String getUserId() {
    return userId;
  }

  protected Instant getTimestamp() {
    return timestamp;
  }

}
