package com.wisdom.eventstoredb;

import java.io.Closeable;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eventstore.dbclient.EventStoreDBClient;

public class ESDBClientWrapper implements Closeable {
  private static final Logger LOGGER = LoggerFactory.getLogger(ESDBClientWrapper.class);
  
  private final EventStoreDBClient client;

  ESDBClientWrapper(EventStoreDBClient client) {
    this.client = client;
  }

  public EventStoreDBClient getClient() {
    return client;
  }

  @Override
  public void close() {
    try {
      client.shutdown();
    } catch (ExecutionException | InterruptedException e) {
      LOGGER.error("An exception was encountered while closing the client instance", e);
    }
  }

}
