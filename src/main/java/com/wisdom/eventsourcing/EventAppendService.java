package com.wisdom.eventsourcing;

import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.eventstore.dbclient.AppendToStreamOptions;
import com.eventstore.dbclient.EventData;
import com.eventstore.dbclient.ExpectedRevision;
import com.eventstore.dbclient.WriteResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wisdom.eventstoredb.EventStoreDBProvider;

@Service
public class EventAppendService {
	private static final Logger LOGGER = LoggerFactory.getLogger(EventAppendService.class);

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private EventStoreDBProvider provider;

	/**
	 * @param client
	 * @param options
	 * @return
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public WriteResult appendToStream(EventAppendBuilder buffer)
			throws InterruptedException, ExecutionException {
		var options = AppendToStreamOptions.get();
		Iterator<EventData> eventDataIterator = buffer.getEvents().stream().map(event -> {
			try {
				return new EventData(UUID.randomUUID(), event.getEventType(), MediaType.APPLICATION_JSON.toString(),
						mapper.writeValueAsBytes(event), null);
			} catch (JsonProcessingException e) {
				LOGGER.error("An error occurred while trying to serialize an event", e);
				return null;
			}
		}).iterator();

		if (buffer.getExpectedRevision() != null) {
			if (options.getExpectedRevision() != null) {
				LOGGER.debug("Replaced original expectedRevision value of {} with {}", options.getExpectedRevision(),
						buffer.getExpectedRevision());
			}

			options.expectedRevision(buffer.getExpectedRevision());
		} else {
			options.expectedRevision(ExpectedRevision.ANY);
		}

		LOGGER.debug("Pushing to stream {} expecting revision {}", buffer.getStreamId(), options.getExpectedRevision().toString());
		return provider.getClient().appendToStream(buffer.getStreamId(), options, eventDataIterator).get();
	}
}
