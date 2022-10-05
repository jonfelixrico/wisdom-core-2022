package com.wisdom.quote.writemodel;

import java.time.Instant;
import java.util.List;

import com.eventstore.dbclient.ExpectedRevision;
import com.wisdom.eventstoredb.utils.EventAppendBuffer;
import com.wisdom.quote.aggregate.Receive;
import com.wisdom.quote.aggregate.VotingSession;
import com.wisdom.quote.entity.QuoteBehavior;
import com.wisdom.quote.entity.QuoteEntity;
import com.wisdom.quote.writemodel.events.QuoteApprovedBySystemEvent;
import com.wisdom.quote.writemodel.events.QuoteFlaggedAsExpiredBySystemEvent;
import com.wisdom.quote.writemodel.events.QuoteReceivedEvent;
import com.wisdom.quote.writemodel.events.QuoteVotesModifiedEvent;

public class QuoteBehaviorImpl extends QuoteBehavior {
	private EventAppendBuffer buffer;

	public QuoteBehaviorImpl(QuoteEntity entity, ExpectedRevision revision) {
		super(entity);
		this.buffer = new EventAppendBuffer(String.format("quote/%s", entity.getId()), revision);
	}

	private String getId() {
		return getEntity().getId();
	}

	public void updateVotingSession(List<String> voterIds, Instant timestamp) {
		super.updateVotingSession(new VotingSession(timestamp, voterIds));
		buffer.pushEvent(new QuoteVotesModifiedEvent(getId(), voterIds, timestamp));
	}

	public void receive(String receiveId, String receiverId, Instant receiveDt, String serverId, String channelId,
			String messageId) {
		super.receive(new Receive(receiveId, receiveDt, receiverId, serverId, channelId, messageId));
		buffer.pushEvent(
				new QuoteReceivedEvent(getId(), receiveId, receiverId, receiveDt, serverId, channelId, messageId));
	}

	public void approveBySystem(Instant timestamp) {
		// TODO Auto-generated method stub
		super.approve(timestamp);
		buffer.pushEvent(new QuoteApprovedBySystemEvent(getId(), timestamp));
	}

	public void flagAsExpiredBySystem(Instant timestamp) {
		super.flagAsExpired(timestamp);
		buffer.pushEvent(new QuoteFlaggedAsExpiredBySystemEvent(getId(), timestamp));
	}

}
