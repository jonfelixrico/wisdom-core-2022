package com.wisdom.quote.service;

import java.time.Instant;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.wisdom.quote.projection.snapshot.QuoteMongoRepository;
import com.wisdom.quote.writemodel.QuoteWriteModelRepository;

@Service
public class PendingQuoteService {
	private static final Logger LOGGER = LoggerFactory.getLogger(PendingQuoteService.class);

	@Autowired
	private QuoteMongoRepository mongoRepo;

	@Autowired
	private QuoteWriteModelRepository writeRepo;

	public PendingQuoteServiceModel getPendingQuote(String quoteId, String serverId) {
		var result = mongoRepo.getPendingQuoteByIdAndServer(quoteId, serverId);
		return result == null ? null : new PendingQuoteServiceModel(result);
	}

	public List<PendingQuoteServiceModel> getPendingQuotes(String serverId) {
		var results = mongoRepo.getPendingQuotesByServer(serverId);
		return results.stream().map(i -> new PendingQuoteServiceModel(i)).toList();
	}

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
