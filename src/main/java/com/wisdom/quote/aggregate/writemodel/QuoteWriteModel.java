package com.wisdom.quote.aggregate.writemodel;

import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;

import com.wisdom.eventsourcing.EventsBuffer;
import com.wisdom.quote.aggregate.QuoteAggregate;
import com.wisdom.quote.aggregate.VoteType;
import com.wisdom.quote.aggregate.events.QuoteApprovedBySystemEvent;
import com.wisdom.quote.aggregate.events.QuoteFlaggedAsExpiredBySystemEvent;
import com.wisdom.quote.aggregate.events.QuoteReceivedEvent;
import com.wisdom.quote.aggregate.events.QuoteSubmittedEvent;
import com.wisdom.quote.aggregate.events.QuoteVoteAddedEvent;

public class QuoteWriteModel {
	private static String getStreamId(String quoteId) {
		return String.format("quote/%s", quoteId);
	}

	public static QuoteWriteModel submit(String quoteId, String content, String authorId, String submitterId,
			Instant createDt, Instant expirationDt, String serverId, String channelId, String messageId) {
		// push the initial event
		EventsBuffer buffer = new EventsBuffer(getStreamId(quoteId), BigInteger.ZERO);
		buffer.pushEvent(new QuoteSubmittedEvent(quoteId, content, authorId, submitterId, createDt, expirationDt,
				serverId, channelId, messageId));

		QuoteAggregate aggregate = new QuoteAggregate(expirationDt, new HashMap<>(), new ArrayList<>(), null);
		return new QuoteWriteModel(quoteId, aggregate, buffer);
	}

	private String quoteId;

	private QuoteAggregate aggregate;
	private EventsBuffer buffer;

	public QuoteWriteModel(String quoteId, QuoteAggregate aggregate, BigInteger revision) {
		this(quoteId, aggregate, new EventsBuffer(getStreamId(quoteId), revision));
	}

	private QuoteWriteModel(String quoteId, QuoteAggregate aggregate, EventsBuffer buffer) {
		this.quoteId = quoteId;
		this.aggregate = aggregate;
		this.buffer = buffer;
	}

	public String getQuoteId() {
		return quoteId;
	}

	public void addVote(String voterId, VoteType voteType, Instant voteDt) {
		aggregate.addVote(voterId, voteType, voteDt);
		buffer.pushEvent(new QuoteVoteAddedEvent(quoteId, voterId, voteType, voteDt));
	}

	public void receive(String receiveId, String receiverId, Instant receiveDt, String serverId, String channelId,
			String messageId) {
		aggregate.receive(receiveId);
		buffer.pushEvent(
				new QuoteReceivedEvent(quoteId, receiveId, receiverId, receiveDt, serverId, channelId, messageId));
	}

	public void approveBySystem(Instant timestamp) {
		aggregate.approve(timestamp);
		buffer.pushEvent(new QuoteApprovedBySystemEvent(quoteId, timestamp));
	}

	public void flagAsSystemAsExpired(Instant timestamp) {
		aggregate.flagAsExpired(timestamp);
		buffer.pushEvent(new QuoteFlaggedAsExpiredBySystemEvent(quoteId, timestamp));
	}
}
