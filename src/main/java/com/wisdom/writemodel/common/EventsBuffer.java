package com.wisdom.writemodel.common;

import java.util.ArrayList;
import java.util.List;

import com.wisdom.eventsourcing.Event;

public class EventsBuffer {
	private String streamId;
	private List<Event> events;

	public EventsBuffer(String streamId) {
		this.streamId = streamId;
		this.events = new ArrayList<>();
	}
	
	public String getStreamId() {
		return streamId;
	}

	public List<Event> getEvents() {
		return List.copyOf(events);
	}
	
	public void pushEvent(Event event) {
		events.add(event);
	}
}
