package com.wisdom.common.writemodel;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Event {
	@JsonIgnore
	String getEventType();
}
