package com.wisdom.quote.writemodel;

import java.time.Instant;
import java.util.List;

import com.eventstore.dbclient.ExpectedRevision;
import com.wisdom.eventstoredb.utils.EventAppendBuffer;
import com.wisdom.eventstoredb.utils.EventAppendService;
import com.wisdom.quote.entity.QuoteBehavior;
import com.wisdom.quote.entity.QuoteEntity;
import com.wisdom.quote.entity.Receive;
import com.wisdom.quote.entity.VotingSession;
import com.wisdom.quote.writemodel.events.QuoteApprovedBySystemEvent;
import com.wisdom.quote.writemodel.events.QuoteFlaggedAsExpiredBySystemEvent;
import com.wisdom.quote.writemodel.events.QuoteReceivedEvent;
import com.wisdom.quote.writemodel.events.QuoteVotesModifiedEvent;

public class QuoteWriteModel extends QuoteBehavior {
	private EventAppendBuffer buffer;
	private EventAppendService writeSvc;

	QuoteWriteModel(QuoteEntity entity, ExpectedRevision revision, EventAppendService writeSvc) {
		super(entity);
		this.buffer = new EventAppendBuffer(String.format("quote/%s", entity.getId()), revision);
		this.writeSvc = writeSvc;
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

	public void save() throws Exception {
		this.writeSvc.appendToStream(buffer);
	}

	EventAppendBuffer getBuffer() {
		return buffer;
	}

}
