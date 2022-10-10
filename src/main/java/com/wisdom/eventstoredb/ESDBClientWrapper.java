package com.wisdom.eventstoredb;

import com.eventstore.dbclient.EventStoreDBClient;

public class ESDBClientWrapper implements AutoCloseable {
  private EventStoreDBClient client;

  ESDBClientWrapper(EventStoreDBClient client) {
    this.client = client;
  }

  protected EventStoreDBClient getClient() {
    return client;
  }

  @Override
  public void close() throws Exception {
    this.client.shutdown();
  }

}
