package com.wisdom.quote.aggregates;

public interface Vote {
	String getVoterId();
	VoteType type();
}
