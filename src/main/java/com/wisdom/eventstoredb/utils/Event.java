package com.wisdom.eventstoredb.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Event {
	@JsonIgnore
	String getEventType();
}
