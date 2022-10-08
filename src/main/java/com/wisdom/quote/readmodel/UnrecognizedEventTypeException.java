package com.wisdom.quote.readmodel;

class UnrecognizedEventTypeException extends Exception {
	private static final long serialVersionUID = 5958218581284224532L;

	private String eventType;

	public UnrecognizedEventTypeException(String eventType) {
		this.eventType = eventType;
	}

	public String getEventType() {
		return eventType;
	}

}
