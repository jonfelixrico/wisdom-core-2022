package com.wisdom.quote.readmodel;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuoteReadModelRepository {
	@Autowired
	private QuoteReadMDBRepository repo;

	public QuoteReadModel findById(String quoteId) {
		var results = repo.findById(quoteId);
		return results.isEmpty() ? null : results.get();
	}

	public List<QuoteReadModel> findPendingQuotesInServer(String serverId) {
		return new ArrayList<QuoteReadModel>(repo.getPendingQuotesByServerId(serverId));
	}

	public QuoteReadModel getRandomQuoteInServer(String serverId) {
		var fromDb = repo.getRandomQuoteByServerId(serverId);
		return fromDb.isEmpty() ? null : fromDb.get(0);
	}
}