package com.wisdom.quote.service;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.wisdom.quote.projection.QuoteProjectionService;
import com.wisdom.quote.projection.snapshot.QuoteMongoRepository;
import com.wisdom.quote.writemodel.QuoteWriteModelRepository;

@Service
public class PendingQuoteService {
	private static final Logger LOGGER = LoggerFactory.getLogger(PendingQuoteService.class);

	@Autowired
	private QuoteMongoRepository mongoRepo;

	@Autowired
	private QuoteWriteModelRepository writeRepo;

	@Autowired
	private QuoteProjectionService projSvc;

	public PendingQuoteServiceModel getPendingQuote(String quoteId, String serverId) {
		var result = mongoRepo.getPendingQuoteByIdAndServer(quoteId, serverId);
		return result == null ? null : new PendingQuoteServiceModel(result);
	}

	public List<PendingQuoteServiceModel> getPendingQuotes(String serverId) {
		var results = mongoRepo.getPendingQuotesByServer(serverId);
		return results.stream().map(i -> new PendingQuoteServiceModel(i)).toList();
	}

	public void flagQuoteAsExpired(String quoteId, String serverId)
			throws InterruptedException, ExecutionException, IOException {
		var projection = projSvc.getProjection(quoteId);
		if (projection == null) {
			LOGGER.debug("Did not find projection for {}", quoteId);
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
		writeModel.flagAsSystemAsExpired(null);
		writeRepo.saveWriteModel(writeModel);
	}

	public void approveQuote(String quoteId, String serverId)
			throws InterruptedException, ExecutionException, IOException {
		var projection = projSvc.getProjection(quoteId);
		if (projection == null) {
			LOGGER.debug("Did not find projection for {}", quoteId);
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
		writeModel.approveBySystem(Instant.now());
		writeRepo.saveWriteModel(writeModel);
	}

	@Deprecated
	@Scheduled(cron = "0 */5 * * * *")
	private void doExpirationFlagging() {
		var ids = mongoRepo.getAllPendingQuotesForExpirationFlagging(Instant.now()).stream().map(i -> i.getId())
				.toList();

		if (ids.size() == 0) {
			LOGGER.debug("Prematurely terminated expired quotes sweep: no expired quotes found");
			return;
		}

		LOGGER.debug("Attempting to flag {} quotes as expired", ids.size());
		int successCount = 0;
		for (String id : ids) {
			try {
				var writeModel = writeRepo.getWriteModel(id);
				writeModel.flagAsExpired(Instant.now());
				writeRepo.saveWriteModel(writeModel);
				LOGGER.debug("Flagged {} as expired", id);
				successCount++;
			} catch (Exception e) {
				LOGGER.error("An exception was encountered while trying to flag {} as expired", id, e);
			}
		}

		LOGGER.debug("Succesfully flagged {} out of {} quotes as expired", successCount, ids.size());
	}
}
