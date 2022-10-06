package com.wisdom.quote.writemodel;

import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eventstore.dbclient.ExpectedRevision;
import com.wisdom.eventstoredb.utils.EventAppendService;
import com.wisdom.quote.entity.QuoteEntity;
import com.wisdom.quote.projection.QuoteProjectionService;
import com.wisdom.quote.writemodel.events.QuoteSubmittedEvent;

@Service
public class QuoteWriteService {

	@Autowired
	EventAppendService eventAppendService;

	@Autowired
	QuoteProjectionService projSvc;

	public QuoteWriteModelV2 create(String quoteId, String content, String authorId, String submitterId,
			Instant createDt, Instant expirationDt, String serverId, String channelId, String messageId,
			int requiredVoteCount) {
		var entity = new QuoteEntity(quoteId, content, authorId, submitterId, createDt, expirationDt, serverId,
				channelId, messageId, null, null, null, null);
		var writeModel = new QuoteWriteModelV2(entity, ExpectedRevision.NO_STREAM, eventAppendService);

		writeModel.getBuffer().pushEvent(new QuoteSubmittedEvent(quoteId, content, authorId, submitterId, createDt,
				expirationDt, serverId, channelId, messageId, requiredVoteCount)); // TODO adjust the required vote
																					// count
		return writeModel;

	}

	public QuoteWriteModelV2 get(String quoteId) throws InterruptedException, ExecutionException, IOException {
		var result = projSvc.getProjection(quoteId);
		if (result == null) {
			return null;
		}

		return new QuoteWriteModelV2(result, ExpectedRevision.expectedRevision(result.getRevision()),
				eventAppendService);
	}
}