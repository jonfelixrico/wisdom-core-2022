package com.wisdom.quote.writemodel;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;

import com.wisdom.eventsourcing.EventAppendBuilder;
import com.wisdom.quote.aggregate.QuoteAggregate;
import com.wisdom.quote.aggregate.VoteType;
import com.wisdom.quote.writemodel.events.QuoteApprovedBySystemEvent;
import com.wisdom.quote.writemodel.events.QuoteFlaggedAsExpiredBySystemEvent;
import com.wisdom.quote.writemodel.events.QuoteReceivedEvent;
import com.wisdom.quote.writemodel.events.QuoteSubmittedEvent;
import com.wisdom.quote.writemodel.events.QuoteVoteAddedEvent;

public class QuoteWriteModel {
	private static String getStreamId(String quoteId) {
		return String.format("quote/%s", quoteId);
	}

	public static QuoteWriteModel submit(String quoteId, String content, String authorId, String submitterId,
			Instant createDt, Instant expirationDt, String serverId, String channelId, String messageId) {
		// push the initial event
		EventAppendBuilder builder = new EventAppendBuilder(getStreamId(quoteId), 0L);
		builder.pushEvent(new QuoteSubmittedEvent(quoteId, content, authorId, submitterId, createDt, expirationDt,
				serverId, channelId, messageId));

		QuoteAggregate aggregate = new QuoteAggregate(expirationDt, new HashMap<>(), new ArrayList<>(), null);
		return new QuoteWriteModel(quoteId, aggregate, builder);
	}

	private String quoteId;

	private QuoteAggregate aggregate;
	private EventAppendBuilder builder;

	public QuoteWriteModel(String quoteId, QuoteAggregate aggregate, long expectedRevision) {
		this(quoteId, aggregate, new EventAppendBuilder(getStreamId(quoteId), expectedRevision));
	}

	/**
	 * This version of the constructor was necessary because of the builder part. In the quote submission event,
	 * we want to be able to push an event which is not covered by one of the methods here.
	 * 
	 * @param quoteId
	 * @param aggregate
	 * @param builder
	 */
	private QuoteWriteModel(String quoteId, QuoteAggregate aggregate, EventAppendBuilder builder) {
		this.quoteId = quoteId;
		this.aggregate = aggregate;
		this.builder = builder;
	}

	public String getQuoteId() {
		return quoteId;
	}

	public void addVote(String voterId, VoteType voteType, Instant voteDt) {
		aggregate.addVote(voterId, voteType, voteDt);
		builder.pushEvent(new QuoteVoteAddedEvent(quoteId, voterId, voteType, voteDt));
	}

	public void receive(String receiveId, String receiverId, Instant receiveDt, String serverId, String channelId,
			String messageId) {
		aggregate.receive(receiveId);
		builder.pushEvent(
				new QuoteReceivedEvent(quoteId, receiveId, receiverId, receiveDt, serverId, channelId, messageId));
	}

	public void approveBySystem(Instant timestamp) {
		aggregate.approve(timestamp);
		builder.pushEvent(new QuoteApprovedBySystemEvent(quoteId, timestamp));
	}

	public void flagAsSystemAsExpired(Instant timestamp) {
		aggregate.flagAsExpired(timestamp);
		builder.pushEvent(new QuoteFlaggedAsExpiredBySystemEvent(quoteId, timestamp));
	}
}
