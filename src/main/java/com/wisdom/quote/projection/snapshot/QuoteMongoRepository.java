package com.wisdom.quote.projection.snapshot;

import java.time.Instant;
import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

@Deprecated
public interface QuoteMongoRepository extends MongoRepository<QuoteMongoModel, String> {
	@Query("{ serverId: '?0', verdict: null }")
	public List<QuoteMongoModel> getPendingQuotesByServer(String serverId);

	@Query("{ serverId: '?0', verdict: null }")
	public QuoteMongoModel getPendingQuoteByIdAndServer(String quoteId, String serverId);

	@Query("{ serverId: '?0', 'verdict.status': 'APPROVED' }")
	public List<QuoteMongoModel> getQuotesByServer(String serverId);

	@Query("{ serverId: '?0', 'verdict.status': 'APPROVED' }")
	public QuoteMongoModel getQuoteByIdAndServer(String serverId);

	@Query(value = "{ verdict: null, expirationDt: { lt: '?0' } }", fields = "{ 'id': 1 }")
	public List<QuoteMongoModel> getAllPendingQuotesForExpirationFlagging(Instant referenceDt);

	@Aggregation(pipeline = { "{ $match: { serverId: '?0', 'verdict.status': 'APPROVED' } }",
			"{ $sample: { size: 1 } }" })
	public List<QuoteMongoModel> getRandomQuote(String serverId);

}
