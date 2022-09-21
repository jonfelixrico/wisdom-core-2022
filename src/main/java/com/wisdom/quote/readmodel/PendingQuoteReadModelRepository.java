package com.wisdom.quote.readmodel;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wisdom.quote.projection.snapshot.QuoteMongoRepository;

@Service
public class PendingQuoteReadModelRepository {
	@Autowired
	private QuoteMongoRepository repo;

	public PendingQuoteReadModel getPendingQuote(String quoteId, String serverId) {
		var result = repo.getPendingQuoteByIdAndServer(quoteId, serverId);
		return result == null ? null : new PendingQuoteReadModel(result);
	}

	public List<PendingQuoteReadModel> getPendingQuotes(String serverId) {
		var results = repo.getPendingQuotesByServer(serverId);
		return results.stream().map(i -> new PendingQuoteReadModel(i)).collect(Collectors.toList());
	}
}
