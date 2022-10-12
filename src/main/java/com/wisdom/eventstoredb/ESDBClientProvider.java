package com.wisdom.eventstoredb;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.eventstore.dbclient.EventStoreDBClient;
import com.eventstore.dbclient.EventStoreDBClientSettings;
import com.eventstore.dbclient.EventStoreDBConnectionString;

@Service
public class ESDBClientProvider {
  @Value("${esdb.connectionString}")
  private String esdbUri;

  private EventStoreDBClientSettings cachedSettings;

  private EventStoreDBClientSettings getSettings() {
    if (cachedSettings == null) {
      cachedSettings = EventStoreDBConnectionString.parseOrThrow(esdbUri);
    }

    return cachedSettings;
  }

  public ESDBClientWrapper getWrapped() {
    return new ESDBClientWrapper(getRaw());
  }

  public EventStoreDBClient getRaw() {
    return EventStoreDBClient.create(getSettings());
  }
}
