package com.wisdom.eventsourcing;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Event {
	@JsonIgnore
	String getEventType();
}
