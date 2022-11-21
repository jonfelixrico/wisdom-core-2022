package com.wisdom.quote.eventsourcing;

import java.util.Map;

public class RecordedQuoteEvent {
  private Long revision;
  private Map<String, Object> data;
  private String eventType;

  public RecordedQuoteEvent(Long revision, Map<String, Object> data, String eventType) {
    this.revision = revision;
    this.data = data;
    this.eventType = eventType;
  }

  public Long getRevision() {
    return revision;
  }

  public Map<String, Object> getData() {
    return data;
  }

  public String getEventType() {
    return eventType;
  }
}
