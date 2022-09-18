package com.wisdom.quote.writemodel;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eventstore.dbclient.ExpectedRevision;
import com.wisdom.eventstoredb.utils.EventAppendService;
import com.wisdom.quote.projection.QuoteProjectionService;
import com.wisdom.quote.writemodel.events.QuoteSubmittedEvent;

@Service
public class QuoteWriteModelRepository {
	@Autowired
	QuoteProjectionService projectionService;

	@Autowired
	EventAppendService eventAppendService;

	public QuoteWriteModel create(String quoteId, String content, String authorId, String submitterId, Instant createDt,
			Instant expirationDt, String serverId, String channelId, String messageId) {
		var model = new QuoteWriteModel(expirationDt, new ArrayList<>(), new ArrayList<>(), null, quoteId,
				// TODO change the value to something from the DB
				3, ExpectedRevision.NO_STREAM);

		model.getEventBuffer().pushEvent(new QuoteSubmittedEvent(quoteId, content, authorId, submitterId, createDt,
				expirationDt, serverId, channelId, messageId, 3)); // TODO adjust the required vote count

		return model;

	}

	public QuoteWriteModel getWriteModel(String quoteId) throws InterruptedException, ExecutionException, IOException {
		var result = projectionService.getProjection(quoteId);
		var data = result.getFirst();

		var receives = data.getReceives().stream().map(r -> r.getId()).collect(Collectors.toList());
		return new QuoteWriteModel(data.getExpirationDt(), data.getVoterIds(), receives, data.getVerdict(),
				data.getId(), data.getRequiredVoteCount(), ExpectedRevision.expectedRevision(result.getSecond()));
	}

	public void saveWriteModel(QuoteWriteModel model) throws InterruptedException, ExecutionException {
		eventAppendService.appendToStream(model.getEventBuffer());
	}
}
