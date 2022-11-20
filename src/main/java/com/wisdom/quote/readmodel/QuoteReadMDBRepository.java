package com.wisdom.quote.readmodel;

import java.time.Instant;
import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

interface QuoteReadMDBRepository extends MongoRepository<QuoteReadDBModel, String> {
  @Query("{ serverId: ?0, statusDeclaration: null }")
  public List<QuoteReadDBModel> getPendingQuotes(String serverId);

  /**
   * 
   * @return This list will be empty or will contain only one item.
   */
  @Aggregation(pipeline = {
      "{ $match: { serverId: ?0, 'statusDeclaration.status': 'APPROVED' } }", "{ $sample: { size: 1 }}" })
  public List<QuoteReadDBModel> getRandomQuoteByServerId(String serverId);
  
  @Query("{ serverId: ?0, 'statusDeclaration.status': 'APPROVED' }")
  public List<QuoteReadDBModel> getQuotesByServerId(String serverId);
  
  @Query("{ serverId: ?0, authorId: ?1, 'statusDeclaration.status': 'APPROVED' }")
  public List<QuoteReadDBModel> getQuotesByServerIdAndAuthorId(String serverId, String authorId);

  /**
   * 
   * @return This list will be empty or will contain only one item.
   */
  @Aggregation(pipeline = {
      "{ $match: { serverId: ?0, authorId: ?1, 'statusDeclaration.status': 'APPROVED' } }", "{ $sample: { size: 1 }}" })
  public List<QuoteReadDBModel> getRandomQuoteByServerIdAndAuthorId(String serverId, String authorId);
  
  @Query("{ serverId: ?0, statusDeclaration: null, expirationDt: { $lt: ?1 } }")
  public List<QuoteReadDBModel> getExpiringPendingQuotes (String serverId, Instant referenceDt);
}
