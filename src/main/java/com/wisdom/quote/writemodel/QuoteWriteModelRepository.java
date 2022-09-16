package com.wisdom.quote.writemodel;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wisdom.common.writemodel.EventAppendService;
import com.wisdom.quote.aggregate.QuoteAggregate;
import com.wisdom.quote.projection.QuoteProjectionService;
import com.wisdom.quote.writemodel.events.QuoteSubmittedEvent;
import com.wisdom.quote.projection.QuoteProjectionModel;

@Service
public class QuoteWriteModelRepository {
	@Autowired
	QuoteProjectionService projectionService;

	@Autowired
	EventAppendService eventAppendService;

	private static QuoteAggregate projectionToAggregate(QuoteProjectionModel projection) {
		List<String> receives = projection.getReceives().stream().map(r -> r.getId()).collect(Collectors.toList());

		return new QuoteAggregate(projection.getExpirationDt(), projection.getVoterIds(), receives,
				projection.getVerdict());
	}

	public QuoteWriteModel create(String quoteId, String content, String authorId, String submitterId, Instant createDt,
			Instant expirationDt, String serverId, String channelId, String messageId) {
		var aggregate = new QuoteAggregate(expirationDt, new ArrayList<>(), new ArrayList<>(), null);

		var model = new QuoteWriteModel(quoteId, aggregate, null);
		model.getEventBuffer().pushEvent(new QuoteSubmittedEvent(quoteId, content, authorId, submitterId, createDt,
				expirationDt, serverId, channelId, messageId));

		return model;

	}

	public QuoteWriteModel getWriteModel(String quoteId) throws InterruptedException, ExecutionException, IOException {
		var proj = projectionService.getProjection(quoteId);
		return new QuoteWriteModel(quoteId, projectionToAggregate(proj.getFirst()), proj.getSecond());
	}

	public void saveWriteModel(QuoteWriteModel model) throws InterruptedException, ExecutionException {
		eventAppendService.appendToStream(model.getEventBuffer());
	}
}
