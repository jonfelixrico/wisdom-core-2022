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
public class PendingQuoteReadModelRepository {
	private static final Logger LOGGER = LoggerFactory.getLogger(PendingQuoteReadModelRepository.class);

	@Autowired
	private QuoteMongoRepository mongoRepo;

	@Autowired
	private QuoteWriteModelRepository writeRepo;

	public PendingQuoteReadModel getPendingQuote(String quoteId, String serverId) {
		var result = mongoRepo.getPendingQuoteByIdAndServer(quoteId, serverId);
		return result == null ? null : new PendingQuoteReadModel(result);
	}

	public List<PendingQuoteReadModel> getPendingQuotes(String serverId) {
		var results = mongoRepo.getPendingQuotesByServer(serverId);
		return results.stream().map(i -> new PendingQuoteReadModel(i)).toList();
	}

	@Scheduled(cron = "0 */1 * * * *")
	private void doExpirationFlagging() {
		var ids = mongoRepo.getAllPendingQuotesForExpirationFlagging(Instant.now()).stream().map(i -> i.getId())
				.toList();
		
		if (ids.size() == 0) {
			LOGGER.debug("Prematurely terminated expired quotes sweep: no expired quotes found");
			return;
		}

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
