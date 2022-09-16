package com.wisdom.quote.writemodel;

import java.time.Instant;
import java.util.List;

import com.eventstore.dbclient.ExpectedRevision;
import com.wisdom.common.writemodel.EventAppendBuffer;
import com.wisdom.quote.aggregate.QuoteAggregate;
import com.wisdom.quote.aggregate.Verdict;
import com.wisdom.quote.writemodel.events.QuoteApprovedBySystemEvent;
import com.wisdom.quote.writemodel.events.QuoteFlaggedAsExpiredBySystemEvent;
import com.wisdom.quote.writemodel.events.QuoteReceivedEvent;
import com.wisdom.quote.writemodel.events.QuoteVotesModifiedEvent;

public class QuoteWriteModel extends QuoteAggregate {
	private String quoteId;
	private Integer requiredVoteCount;
	private EventAppendBuffer buffer;

	QuoteWriteModel(Instant expirationDt, List<String> voterIds, List<String> receiveIds, Verdict verdict,
			String quoteId, Integer requiredVoteCount, ExpectedRevision revision) {
		super(expirationDt, voterIds, receiveIds, verdict);
		this.quoteId = quoteId;
		this.requiredVoteCount = requiredVoteCount;
		this.buffer = new EventAppendBuffer(String.format("quote/%s", quoteId), revision);
	}

	public Integer getRequiredVoteCount() {
		return requiredVoteCount;
	}

	public String getQuoteId() {
		return quoteId;
	}

	public void receive(String receiveId, String receiverId, Instant receiveDt, String serverId, String channelId,
			String messageId) {
		super.receive(receiveId);
		buffer.pushEvent(
				new QuoteReceivedEvent(quoteId, receiveId, receiverId, receiveDt, serverId, channelId, messageId));
	}

	public void approveBySystem(Instant timestamp) {
		super.approve(timestamp);
		buffer.pushEvent(new QuoteApprovedBySystemEvent(quoteId, timestamp));
	}

	public void flagAsSystemAsExpired(Instant timestamp) {
		super.flagAsExpired(timestamp);
		buffer.pushEvent(new QuoteFlaggedAsExpiredBySystemEvent(quoteId, timestamp));
	}

	public void setVoters(List<String> voterIds, Instant timestamp) {
		super.setVotes(voterIds);
		buffer.pushEvent(new QuoteVotesModifiedEvent(quoteId, voterIds, timestamp));
	}

	EventAppendBuffer getEventBuffer() {
		return buffer;
	}
}
