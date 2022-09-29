package com.wisdom.quote.service;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wisdom.quote.projection.QuoteProjectionService;
import com.wisdom.quote.projection.snapshot.QuoteMongoRepository;
import com.wisdom.quote.writemodel.QuoteWriteModelRepository;

@Service
public class QuoteService {
	private static final Logger LOGGER = LoggerFactory.getLogger(QuoteService.class);

	@Autowired
	private QuoteMongoRepository repo;

	@Autowired
	private QuoteWriteModelRepository writeRepo;

	@Autowired
	private QuoteProjectionService projSvc;

	public QuoteServiceModel getRandomQuote(String serverId) {
		var results = repo.getRandomQuote(serverId);
		if (results.size() == 0) {
			return null;
		}

		return new QuoteServiceModel(results.get(0));
	}

	public void receiveQuote(String quoteId, String serverId, String receiverId, String channelId, String messageId)
			throws InterruptedException, ExecutionException, IOException {
		var projection = projSvc.getProjection(quoteId);
		if (projection == null) {
			LOGGER.debug("Did not find projeciton for {}", quoteId);
			// TODO throw error
			return;
		}

		var projModel = projection.getFirst();
		if (!projModel.getServerId().equals(serverId)) {
			LOGGER.debug("Found projection for {}, but server id mismatched (expected {}, actual {})", quoteId,
					serverId, projModel.getServerId());
			// TODO throw error
			return;
		}

		var writeModel = writeRepo.convertToWriteModel(projModel, projection.getSecond());
		writeModel.receive(UUID.randomUUID().toString(), receiverId, Instant.now(), serverId, channelId, messageId);
		writeRepo.saveWriteModel(writeModel);
	}
}
