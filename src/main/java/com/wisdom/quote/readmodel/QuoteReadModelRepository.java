package com.wisdom.quote.readmodel;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuoteReadModelRepository {
  @Autowired
  private QuoteReadMDBRepository repo;

  public QuoteReadModel findById(String quoteId) {
    var results = repo.findById(quoteId);
    return results.isEmpty() ? null : QuoteReadModel.dbToDto(results.get());
  }

  public List<QuoteReadModel> findPendingQuotesInServer(String serverId) {
    return repo.getPendingQuotes(serverId).stream().map(q -> QuoteReadModel.dbToDto(q)).toList();
  }

  public QuoteReadModel getRandomQuoteInServer(String serverId) {
    var fromDb = repo.getRandomQuoteByServerId(serverId);
    return fromDb.isEmpty() ? null : QuoteReadModel.dbToDto(fromDb.get(0));
  }

  public QuoteReadModel getRandomQuoteInServerFilteredByAuthor(String serverId, String authorId) {
    var fromDb = repo.getRandomQuoteByServerIdAndAuthorId(serverId, authorId);
    return fromDb.isEmpty() ? null : QuoteReadModel.dbToDto(fromDb.get(0));
  }

  public List<QuoteReadModel> getExpiringPendingeQuotes(String serverId, Instant referenceDt) {
    return repo.getExpiringPendingQuotes(serverId, referenceDt).stream().map(q -> QuoteReadModel.dbToDto(q)).toList();
  }

  public List<QuoteReadModel> getServerQuotes(String serverId) {
    return repo.getRandomQuoteByServerId(serverId).stream().map(q -> QuoteReadModel.dbToDto(q)).toList();
  }

  public List<QuoteReadModel> getServerQuotes(String serverId, String authorId) {
    return repo.getRandomQuoteByServerIdAndAuthorId(serverId, authorId).stream().map(q -> QuoteReadModel.dbToDto(q))
        .toList();
  }
}
