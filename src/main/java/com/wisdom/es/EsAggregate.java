package com.wisdom.es;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.util.Pair;

public interface EsAggregate {
	List<Pair<String, Object>> getEvents();
	
	/**
	 * 
	 * @return The id of the event stream that the events will be appended to.
	 */
	String getEventStream();
	
	/**
	 * The revision number of the aggregate instance.
	 * @return
	 */
	BigInteger getRevision();
}
