package com.wisdom.quote.writemodel;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wisdom.eventstoredb.EventStoreDBProvider;
import com.wisdom.quote.aggregate.QuoteAggregate;
import com.wisdom.quote.aggregate.VoteType;
import com.wisdom.quote.projection.QuoteEventsProjectionService;
import com.wisdom.quote.projection.QuoteProjectionModel;

@Service
public class QuoteWriteModelRepository {
	@Autowired
	QuoteEventsProjectionService projectionService;

	@Autowired
	EventStoreDBProvider esdbProvider;

	private static QuoteAggregate projectionToAggregate(QuoteProjectionModel projection) {
		Map<String, VoteType> votes = projection.getVotes().values().stream()
				.collect(Collectors.toMap(v -> v.getUserId(), v -> v.getType()));
		List<String> receives = projection.getReceives().stream().map(r -> r.getId()).collect(Collectors.toList());

		return new QuoteAggregate(projection.getExpirationDt(), votes, receives, projection.getVerdict());
	}

	QuoteWriteModel getWriteModel(String quoteId) throws InterruptedException, ExecutionException, IOException {
		var proj = projectionService.getProjection(quoteId);
		return new QuoteWriteModel(quoteId, projectionToAggregate(proj.getFirst()), proj.getSecond());
	}

	void saveWriteModel(QuoteWriteModel model) throws InterruptedException, ExecutionException {
		model.getEventBuilder().appendtoStream(esdbProvider.getClient());
	}
}