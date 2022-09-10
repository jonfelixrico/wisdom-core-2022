package com.wisdom.eventsourcing;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class EventBuffer {
	private String streamId;
	private List<Event> events;
	private BigInteger revision;

	public EventBuffer(String streamId) {
		this.streamId = streamId;
		this.events = new ArrayList<>();
	}

	public EventBuffer(String streamId, BigInteger revision) {
		this(streamId);
		this.revision = revision;
	}

	public String getStreamId() {
		return streamId;
	}

	public List<Event> getEvents() {
		return List.copyOf(events);
	}

	public BigInteger getRevision() {
		return revision;
	}

	public void pushEvent(Event event) {
		events.add(event);
	}
}
