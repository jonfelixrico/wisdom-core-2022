package com.wisdom.quote.readmodel;

import java.time.Instant;
import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

interface QuoteSnapshotPersistenceRepository extends MongoRepository<QuoteSnapshotPersistence, String> {
  @Query("{ serverId: ?0, statusDeclaration: null }")
  public List<QuoteSnapshotPersistence> getPendingQuotes(String serverId);

  /**
   * 
   * @return This list will be empty or will contain only one item.
   */
  @Aggregation(pipeline = {
      "{ $match: { serverId: ?0, 'statusDeclaration.status': 'APPROVED' } }", "{ $sample: { size: 1 }}" })
  public List<QuoteSnapshotPersistence> getRandomQuoteByServerId(String serverId);
  
  @Query("{ serverId: ?0, 'statusDeclaration.status': 'APPROVED' }")
  public List<QuoteSnapshotPersistence> getQuotesByServerId(String serverId);
  
  @Query("{ serverId: ?0, authorId: ?1, 'statusDeclaration.status': 'APPROVED' }")
  public List<QuoteSnapshotPersistence> getQuotesByServerIdAndAuthorId(String serverId, String authorId);

  /**
   * 
   * @return This list will be empty or will contain only one item.
   */
  @Aggregation(pipeline = {
      "{ $match: { serverId: ?0, authorId: ?1, 'statusDeclaration.status': 'APPROVED' } }", "{ $sample: { size: 1 }}" })
  public List<QuoteSnapshotPersistence> getRandomQuoteByServerIdAndAuthorId(String serverId, String authorId);
  
  @Query("{ serverId: ?0, statusDeclaration: null, expirationDt: { $lt: ?1 } }")
  public List<QuoteSnapshotPersistence> getExpiringPendingQuotes (String serverId, Instant referenceDt);
}
