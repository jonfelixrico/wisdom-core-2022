package com.wisdom.quote.projection.snapshot;

import java.time.Instant;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface QuoteMongoRepository extends MongoRepository<QuoteMongoModel, String> {
	@Query("{ serverId: '?0', verdict: null }")
	public List<QuoteMongoModel> getPendingQuotesByServer(String serverId);
	
	@Query("{ serverId: '?0', verdict: null }")
	public QuoteMongoModel getPendingQuoteByIdAndServer(String quoteId, String serverId);	
	
	@Query("{ serverId: '?0', verdict.status: 'APPROVED' }")
	public List<QuoteMongoModel> getQuotesByServer(String serverId);
	
	@Query("{ serverId: '?0', verdict.status: 'APPROVED' }")
	public QuoteMongoModel getQuoteByIdAndServer(String serverId);
	
	@Query(value="{ verdict: null, expirationDt: { lt: '?0' } }", fields="{ 'id': 1 }")
	public List<QuoteMongoModel> getAllPendingQuotesForExpirationFlagging(Instant referenceDt);
}
