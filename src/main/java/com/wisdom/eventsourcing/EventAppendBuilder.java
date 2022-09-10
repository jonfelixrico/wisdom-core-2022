package com.wisdom.eventsourcing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.lang.NonNull;

import com.eventstore.dbclient.AppendToStreamOptions;
import com.eventstore.dbclient.EventData;
import com.eventstore.dbclient.EventDataBuilder;
import com.eventstore.dbclient.EventStoreDBClient;
import com.eventstore.dbclient.WriteResult;

/**
 * This is a class that helps with pushing several events to the same stream.
 * This is very specific to EventSourceDB.
 * 
 * @author Felix
 *
 */
public class EventAppendBuilder {
	private String streamId;
	private List<Event> events;

	private Long expectedRevision;

	/**
	 * This will create an EventBuffer without an expected revision.
	 * 
	 * @param streamId
	 */
	public EventAppendBuilder(String streamId) {
		this.streamId = streamId;
		this.events = new ArrayList<>();
	}

	/**
	 * This will create an EventBuffer that expects the last stream revision id to
	 * be `revision`.
	 * 
	 * @param streamId
	 * @param expectedRevision The revision no. that we expect the stream to be in.
	 */
	public EventAppendBuilder(String streamId, long expectedRevision) {
		this(streamId);
		this.expectedRevision = expectedRevision;
	}

	public String getStreamId() {
		return streamId;
	}

	/**
	 * Returns an immutable shallow copy of the event sequence built.
	 * 
	 * @return
	 */
	public List<Event> getEvents() {
		return List.copyOf(events);
	}

	/**
	 * @return If null, then that means that we will not apply any revision no.
	 *         checking. If a BigInteger was provided, then the output of this
	 *         buffer will have revision no. checking.
	 */
	public long getExpectedRevision() {
		return expectedRevision;
	}

	public void pushEvent(Event event) {
		events.add(event);
	}

	/**
	 * Uses the data in this buffer to append the data to an actual ESDB client.
	 * 
	 * @param client
	 * @param options
	 * @return
	 */
	public CompletableFuture<WriteResult> appendToStream(EventStoreDBClient client,
			@NonNull AppendToStreamOptions options) {
		Iterator<EventData> eventDataIterator = events.stream()
				.map(event -> EventDataBuilder.json(event.getEventType(), event).build()).iterator();

		if (expectedRevision != null) {
			if (options.getExpectedRevision() != null) {
				// TODO add logging that expectedRevision will be overridden
			}

			options.expectedRevision(expectedRevision);
		}

		return client.appendToStream(streamId, options, eventDataIterator);
	}

	/**
	 * Uses the data in this buffer to append the data to an actual ESDB client.
	 * Default append options will be used.
	 * 
	 * @param client
	 * @return
	 */
	public CompletableFuture<WriteResult> appendtoStream(EventStoreDBClient client) {
		return appendToStream(client, AppendToStreamOptions.get());
	}
}
