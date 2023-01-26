package com.wisdom.quote.readmodel;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.MongoExpression;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.stereotype.Service;

@Service
public class QuoteSnapshotRepository {
  @Autowired
  private QuoteSnapshotPersistenceRepository repo;

  @Autowired
  private MongoTemplate mongoTemplate;

  public QuoteSnapshot findById(String quoteId) {
    var results = repo.findById(quoteId);
    return results.isEmpty() ? null : results.get();
  }

  public List<QuoteSnapshot> findPendingQuotesInServer(String serverId) {
    return new ArrayList<>(repo.getPendingQuotes(serverId));
  }

  public QuoteSnapshot getRandomQuoteInServer(String serverId) {
    var fromDb = repo.getRandomQuoteByServerId(serverId);
    return fromDb.isEmpty() ? null : fromDb.get(0);
  }

  public QuoteSnapshot getRandomQuoteInServerFilteredByAuthor(String serverId, String authorId) {
    var fromDb = repo.getRandomQuoteByServerIdAndAuthorId(serverId, authorId);
    return fromDb.isEmpty() ? null : fromDb.get(0);
  }

  public List<QuoteSnapshot> getExpiringPendingeQuotes(String serverId, Instant referenceDt) {
    return new ArrayList<>(repo.getExpiringPendingQuotes(serverId, referenceDt));
  }

  public List<QuoteSnapshot> getServerQuotes(String serverId) {
    return new ArrayList<>(repo.getRandomQuoteByServerId(serverId));
  }

  public List<QuoteSnapshot> getServerQuotes(String serverId, String authorId) {
    return new ArrayList<>(repo.getRandomQuoteByServerIdAndAuthorId(serverId, authorId));
  }

  /**
   * Lists the pending or approved quotes from the server.
   * Uses cursor-based pagination.
   * The pagination is sorted descending by submission date.
   * 
   * @param serverId
   * @param limit
   * @param after Takes the quotes after the specified "cursor". If null, takes the quotes from the start of the results.
   * @return
   */
  public List<QuoteSnapshot> listServerQuotes(String serverId, int limit, String after) {
    // get only the approved or pending quotes from the specified server
    var matchExpression = MongoExpression
        .create("{ serverId: ?0, statusDeclaration: { $or: [null, { status: 'APPROVED' }] } }", serverId);
    var matchStage = Aggregation.match(AggregationExpression.from(matchExpression));
    var sortStage = Aggregation.sort(Sort.by(Sort.Order.desc("submitDt")));
    var aggregate = Aggregation.newAggregation(matchStage, sortStage);

    /*
     * If after is not provided, then we will start taking from the result set
     * immediately.
     * 
     * Else, this will be false. This will only be true AFTER we've found the
     * "cursor".
     * We start taking items after the cursor.
     * 
     * (taking = pushing into the list)
     */
    boolean isCursorFound = after == null;

    var list = new ArrayList<QuoteSnapshot>();
    try (var resultSet = mongoTemplate.aggregateStream(aggregate, QuoteSnapshotPersistence.class,
        QuoteSnapshotPersistence.class)) {
      while (resultSet.hasNext() ||
      // the limiting mechanism
          list.size() <= limit) {
        var value = resultSet.next();
        if (!isCursorFound) {
          /*
           * If we have found the "cursor", then it's time to start pushing items into the
           * list.
           * This will start in the iteration after the one where the cursor was found.
           */
          isCursorFound = value.getId() == after;
          continue;
        }

        list.add(value);
      }
    }

    return list;
  }
}
