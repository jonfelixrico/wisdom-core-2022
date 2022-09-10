package com.wisdom.quote.aggregate.writemodel;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;

import com.wisdom.eventsourcing.EventBuffer;
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
		EventBuffer buffer = new EventBuffer(getStreamId(quoteId), 0L);
		buffer.pushEvent(new QuoteSubmittedEvent(quoteId, content, authorId, submitterId, createDt, expirationDt,
				serverId, channelId, messageId));

		QuoteAggregate aggregate = new QuoteAggregate(expirationDt, new HashMap<>(), new ArrayList<>(), null);
		return new QuoteWriteModel(quoteId, aggregate, buffer);
	}

	private String quoteId;

	private QuoteAggregate aggregate;
	private EventBuffer buffer;

	public QuoteWriteModel(String quoteId, QuoteAggregate aggregate, long expectedRevision) {
		this(quoteId, aggregate, new EventBuffer(getStreamId(quoteId), expectedRevision));
	}

	/**
	 * This version of the constructor was necessary because of the buffer part. In the quote submission event,
	 * we want to be able to push an event which is not covered by one of the methods here.
	 * 
	 * @param quoteId
	 * @param aggregate
	 * @param buffer
	 */
	private QuoteWriteModel(String quoteId, QuoteAggregate aggregate, EventBuffer buffer) {
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
