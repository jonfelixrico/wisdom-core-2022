package com.wisdom.quote.eventsourcing.events;

import com.wisdom.eventstoredb.utils.Event;

public abstract class BaseQuoteEvent implements Event {
	public abstract String getQuoteId();
}
