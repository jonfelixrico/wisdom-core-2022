package com.wisdom.quote.writemodel;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;

import com.eventstore.dbclient.ExpectedRevision;
import com.wisdom.common.writemodel.EventAppendBuffer;
import com.wisdom.quote.aggregate.QuoteAggregate;
import com.wisdom.quote.aggregate.VoteType;
import com.wisdom.quote.writemodel.events.QuoteApprovedBySystemEvent;
import com.wisdom.quote.writemodel.events.QuoteFlaggedAsExpiredBySystemEvent;
import com.wisdom.quote.writemodel.events.QuoteReceivedEvent;
import com.wisdom.quote.writemodel.events.QuoteSubmittedEvent;
import com.wisdom.quote.writemodel.events.QuoteVoteAddedEvent;
import com.wisdom.quote.writemodel.events.QuoteVoteRemovedEvent;

public class QuoteWriteModel {
	private static String getStreamId(String quoteId) {
		return String.format("quote/%s", quoteId);
	}

	static QuoteWriteModel submit(String quoteId, String content, String authorId, String submitterId,
			Instant createDt, Instant expirationDt, String serverId, String channelId, String messageId) {
		QuoteAggregate aggregate = new QuoteAggregate(expirationDt, new HashMap<>(), new ArrayList<>(), null);

		var model = new QuoteWriteModel(quoteId, aggregate, null);
		model.getEventBuffer().pushEvent(new QuoteSubmittedEvent(quoteId, content, authorId, submitterId, createDt,
				expirationDt, serverId, channelId, messageId));

		return model;

	}

	private String quoteId;

	private QuoteAggregate aggregate;
	private EventAppendBuffer buffer;

	QuoteWriteModel(String quoteId, QuoteAggregate aggregate, Long expectedRevision) {
		this.quoteId = quoteId;
		this.aggregate = aggregate;
		this.buffer = new EventAppendBuffer(getStreamId(quoteId), expectedRevision == null ? ExpectedRevision.NO_STREAM
				: ExpectedRevision.expectedRevision(expectedRevision));
	}

	public String getQuoteId() {
		return quoteId;
	}

	public void addVote(String voterId, VoteType voteType, Instant timestamp) {
		aggregate.addVote(voterId, voteType, timestamp);
		buffer.pushEvent(new QuoteVoteAddedEvent(quoteId, voterId, voteType, timestamp));
	}

	public void removeVote(String voterId, Instant timestamp) {
		aggregate.removeVote(voterId);
		buffer.pushEvent(new QuoteVoteRemovedEvent(quoteId, voterId, timestamp));
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

	EventAppendBuffer getEventBuffer() {
		return buffer;
	}
}
