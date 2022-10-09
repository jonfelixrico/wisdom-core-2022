package com.wisdom.quote.readmodel;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

interface QuoteReadMDBRepository extends MongoRepository<QuoteReadMDB, String> {
	@Query("{ serverId: ?0, 'statusDeclaration': null }")
	public List<QuoteReadMDB> getPendingQuotesByServerId(String serverId);

	/**
	 * 
	 * @return This list will be empty or will contain only one item.
	 */
	@Aggregation(pipeline = { "{ $sample: { size: 1 }}",
			"{ $match: { serverId: ?0, 'statusDeclaration.status': 'APPROVED' } }" })
	public List<QuoteReadMDB> getRandomQuoteByServerId(String serverId);
}
