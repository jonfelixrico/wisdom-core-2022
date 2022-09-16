package com.wisdom.quote.writemodel;

import java.time.Instant;
import java.util.List;

import com.eventstore.dbclient.ExpectedRevision;
import com.wisdom.common.writemodel.EventAppendBuffer;
import com.wisdom.quote.aggregate.QuoteAggregate;
import com.wisdom.quote.writemodel.events.QuoteApprovedBySystemEvent;
import com.wisdom.quote.writemodel.events.QuoteFlaggedAsExpiredBySystemEvent;
import com.wisdom.quote.writemodel.events.QuoteReceivedEvent;
import com.wisdom.quote.writemodel.events.QuoteVotesModifiedEvent;

public class QuoteWriteModel {
	private static String getStreamId(String quoteId) {
		return String.format("quote/%s", quoteId);
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
	
	public void setVoters (List<String> voterIds, Instant timestamp) {
		aggregate.setVotes(voterIds);
		buffer.pushEvent(new QuoteVotesModifiedEvent(quoteId, voterIds, timestamp));
	}

	EventAppendBuffer getEventBuffer() {
		return buffer;
	}
}
