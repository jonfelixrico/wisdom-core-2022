package com.wisdom.eventsourcing;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a class that helps with pushing several events to the same stream.
 * This is very specific to EventSourceDB.
 * 
 * @author Felix
 *
 */
public class EventBuffer {
	private String streamId;
	private List<Event> events;
	
	private BigInteger revision;

	/**
	 * This will create an EventBuffer without an expected revision.
	 * @param streamId
	 */
	public EventBuffer(String streamId) {
		this.streamId = streamId;
		this.events = new ArrayList<>();
	}

	/**
	 * This will create an EventBuffer that expects the last stream revision
	 * id to be `revision`.
	 * @param streamId
	 * @param revision The revision no. that we expect the stream to be in.
	 */
	public EventBuffer(String streamId, BigInteger revision) {
		this(streamId);
		this.revision = revision;
	}

	public String getStreamId() {
		return streamId;
	}

	/**
	 * Returns an immutable shallow copy of the event sequence built.
	 * @return
	 */
	public List<Event> getEvents() {
		return List.copyOf(events);
	}

	/**
	 * @return If null, then that means that we will not apply any revision no. checking.
	 * If a BigInteger was provided, then the output of this buffer will have revision no. checking.
	 */
	public BigInteger getRevision() {
		return revision;
	}

	public void pushEvent(Event event) {
		events.add(event);
	}
}
