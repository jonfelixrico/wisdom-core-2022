package com.wisdom.quote.writemodel;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eventstore.dbclient.ExpectedRevision;
import com.wisdom.eventstoredb.utils.EventAppendService;
import com.wisdom.quote.projection.QuoteProjectionModel;
import com.wisdom.quote.projection.QuoteProjectionService;
import com.wisdom.quote.writemodel.events.QuoteSubmittedEvent;

@Deprecated
@Service
public class QuoteWriteModelRepository {
	private static final Logger LOGGER = LoggerFactory.getLogger(QuoteWriteModelRepository.class);

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
		if (result == null) {
			return null;
		}

		return convertToWriteModel(result.getFirst(), result.getSecond());
	}

	public QuoteWriteModel convertToWriteModel(QuoteProjectionModel projection, Long revision) {
		var receives = projection.getReceives().stream().map(r -> r.getId()).collect(Collectors.toList());
		return new QuoteWriteModel(projection.getExpirationDt(), projection.getVoterIds(), receives,
				projection.getVerdict(), projection.getId(), projection.getRequiredVoteCount(),
				ExpectedRevision.expectedRevision(revision));
	}

	public void saveWriteModel(QuoteWriteModel model) throws InterruptedException, ExecutionException {
		eventAppendService.appendToStream(model.getEventBuffer());
		LOGGER.debug("Added {} events to quote {}", model.getEventBuffer().getEvents().size(), model.getQuoteId());
	}
}
