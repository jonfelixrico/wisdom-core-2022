package com.wisdom.eventstoredb;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.eventstore.dbclient.EventStoreDBClient;
import com.eventstore.dbclient.EventStoreDBClientSettings;
import com.eventstore.dbclient.EventStoreDBConnectionString;

@Service
public class EventStoreDBProvider {
	private static Logger LOGGER = LoggerFactory.getLogger(EventStoreDBProvider.class);

	@Value("${esdb.connectionString}")
	private String esdbUri;

	private EventStoreDBClientSettings getSettings() {
		return EventStoreDBConnectionString.parseOrThrow(esdbUri);
	}

	private CompletableFuture<EventStoreDBClient> clientCf;

	private CompletableFuture<EventStoreDBClient> createClientCf() {
		return CompletableFuture.supplyAsync(() -> {
			LOGGER.info("Asynchronously trying to connect to ESDB");
			var client = EventStoreDBClient.create(getSettings());
			
			LOGGER.info("Successfully established connection.");
			return client;
		});
	}

	public EventStoreDBClient getClient() throws InterruptedException, ExecutionException {
		if (clientCf == null) {
			LOGGER.info("Client not found, attempting to create an instance.");
			clientCf = createClientCf();
		}
		
		return clientCf.get();
	}
}
