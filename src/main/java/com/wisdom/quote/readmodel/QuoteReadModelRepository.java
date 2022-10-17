package com.wisdom.quote.readmodel;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuoteReadModelRepository {
  @Autowired
  private QuoteReadMDBRepository repo;

  public QuoteReadModel findById(String quoteId) {
    var results = repo.findById(quoteId);
    return results.isEmpty() ? null : results.get();
  }

  public List<QuoteReadModel> findPendingQuotesInServer(String serverId) {
    return new ArrayList<QuoteReadModel>(repo.getPendingQuotes(serverId));
  }

  public QuoteReadModel getRandomQuoteInServer(String serverId) {
    var fromDb = repo.getRandomQuoteByServerId(serverId);
    return fromDb.isEmpty() ? null : fromDb.get(0);
  }

  public QuoteReadModel getRandomQuoteInServerFilteredByAuthor(String serverId, String authorId) {
    var fromDb = repo.getRandomQuoteByServerIdAndAuthorId(serverId, authorId);
    return fromDb.isEmpty() ? null : fromDb.get(0);
  }
  
  public List<QuoteReadModel> getExpiringPendingeQuotes (String serverId, Instant referenceDt) {
    return new ArrayList<QuoteReadModel>(repo.getExpiringPendingQuotes(serverId, referenceDt));
  }
}

