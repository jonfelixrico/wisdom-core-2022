package com.wisdom.quote.eventsourcing;

public interface CanRetrieveQuoteData {
  public QuoteReducerModel retrieveById(String quoteId) throws Exception;
}
