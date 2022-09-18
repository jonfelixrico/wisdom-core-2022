package com.wisdom.quote.projection;

import java.time.Instant;
import java.util.List;

import com.wisdom.quote.aggregate.Verdict;

class QuoteProjectionModelImpl extends QuoteProjectionModel {

	public QuoteProjectionModelImpl(String id, String content, String authorId, String submitterId, Instant submitDt,
			Instant expirationDt, String serverId, String channelId, String messageId, List<String> voterIds,
			List<Receive> receives, Verdict verdict, Integer requiredVoteCount) {
		super(id, content, authorId, submitterId, submitDt, expirationDt, serverId, channelId, messageId, voterIds,
				receives, verdict, requiredVoteCount);
	}
}
