package com.wisdom.quote.projection.snapshot;

import java.time.Instant;
import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface QuoteDbRepo extends MongoRepository<QuoteDb, String> {
	@Query("{ serverId: '?0', verdict: null }")
	public List<QuoteDb> getPendingQuotesByServer(String serverId);

	@Query("{ serverId: '?0', verdict: null }")
	public QuoteDb getPendingQuoteByIdAndServer(String quoteId, String serverId);

	@Query("{ serverId: '?0', 'verdict.status': 'APPROVED' }")
	public List<QuoteDb> getQuotesByServer(String serverId);

	@Query("{ serverId: '?0', 'verdict.status': 'APPROVED' }")
	public QuoteDb getQuoteByIdAndServer(String serverId);

	@Query(value = "{ verdict: null, expirationDt: { lt: '?0' } }", fields = "{ 'id': 1 }")
	public List<QuoteDb> getAllPendingQuotesForExpirationFlagging(Instant referenceDt);

	@Aggregation(pipeline = { "{ $match: { serverId: '?0', 'verdict.status': 'APPROVED' } }",
			"{ $sample: { size: 1 } }" })
	public List<QuoteDb> getRandomQuote(String serverId);

}
