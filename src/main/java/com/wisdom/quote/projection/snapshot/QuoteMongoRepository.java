package com.wisdom.quote.projection.snapshot;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface QuoteMongoRepository extends MongoRepository<QuoteMongoModel, String> {
	@Query("{ serverId: '?0', verdict: null }")
	public List<QuoteMongoModel> getPendingQuotes(String serverId);
	
	@Query("{ serverId: '?0', verdict.status: 'APPROVED' }")
	public List<QuoteMongoModel> getQuotes(String serverId);
}
