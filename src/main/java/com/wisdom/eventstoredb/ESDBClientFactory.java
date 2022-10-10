package com.wisdom.eventstoredb;

import org.springframework.beans.factory.annotation.Value;

import com.eventstore.dbclient.EventStoreDBClient;
import com.eventstore.dbclient.EventStoreDBClientSettings;
import com.eventstore.dbclient.EventStoreDBConnectionString;

public class ESDBClientFactory {
  @Value("${esdb.connectionString}")
  private String esdbUri;

  private EventStoreDBClientSettings cachedSettings;

  private EventStoreDBClientSettings getSettings() {
    if (cachedSettings == null) {
      cachedSettings = EventStoreDBConnectionString.parseOrThrow(esdbUri);
    }

    return cachedSettings;
  }

  public ESDBClientWrapper getInstance() {
    var client = EventStoreDBClient.create(getSettings());
    return new ESDBClientWrapper(client);
  }
}
