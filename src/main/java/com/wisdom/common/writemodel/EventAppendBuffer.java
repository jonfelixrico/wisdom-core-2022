package com.wisdom.common.writemodel;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import com.eventstore.dbclient.ExpectedRevision;

/**
 * This is a class that helps with pushing several events to the same stream.
 * This is very specific to EventSourceDB.
 * 
 * @author Felix
 *
 */
public class EventAppendBuffer {
	Logger logger = LoggerFactory.getLogger(EventAppendBuffer.class);

	private String streamId;
	private List<Event> events;

	private ExpectedRevision expectedRevision;

	/**
	 * This will create an EventBuffer without an expected revision.
	 * 
	 * @param streamId
	 */
	public EventAppendBuffer(String streamId) {
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
	public EventAppendBuffer(String streamId, ExpectedRevision expectedRevision) {
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
	public ExpectedRevision getExpectedRevision() {
		return expectedRevision;
	}

	public void pushEvent(@NonNull Event event) {
		events.add(event);
		logger.debug("Pushed event type {} to stream {}", event.getEventType(), streamId);
	}
}
