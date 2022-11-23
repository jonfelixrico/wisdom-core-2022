package com.wisdom.quote.readmodel;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuoteSnapshotRepository {
  @Autowired
  private QuoteSnapshotPersistenceRepository repo;

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
}
