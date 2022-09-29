package com.wisdom.quote.service;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wisdom.quote.projection.QuoteProjectionService;
import com.wisdom.quote.projection.snapshot.QuoteMongoRepository;
import com.wisdom.quote.writemodel.QuoteWriteModelRepository;

@Service
public class QuoteService {
	@Autowired
	private QuoteMongoRepository repo;
	private QuoteWriteModelRepository writeRepo;
	private QuoteProjectionService projSvc;

	public QuoteServiceModel getRandomQuote(String serverId) {
		var results = repo.getRandomQuote(serverId);
		if (results.size() == 0) {
			return null;
		}

		return new QuoteServiceModel(results.get(0));
	}

	public void receiveQuote(ReceiveQuoteInput data) throws InterruptedException, ExecutionException, IOException {
		var projection = projSvc.getProjection(data.getQuoteId());
		if (projection == null) {
			// TODO throw error
			return;
		}

		var projModel = projection.getFirst();
		if (projModel.getServerId() != data.getServerId()) {
			// TODO throw error
			return;
		}

		var writeModel = writeRepo.convertToWriteModel(projModel, projection.getSecond());
		writeModel.receive(UUID.randomUUID().toString(), data.getReceiverId(), Instant.now(), data.getServerId(),
				data.getChannelId(), data.getMessageId());
		writeRepo.saveWriteModel(writeModel);
	}
}
