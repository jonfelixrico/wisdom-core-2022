package com.wisdom.quote.readmodel;

import org.springframework.stereotype.Service;

@Service
public class QuoteReadModelRepository {
	private QuoteReadMDBRepository repo;
	
	public QuoteReadModel findById(String quoteId) {
		var results = repo.findById(quoteId);
		return results.isEmpty() ? null : results.get();
	}
}
