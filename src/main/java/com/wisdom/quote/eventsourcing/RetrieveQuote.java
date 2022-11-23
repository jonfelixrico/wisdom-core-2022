package com.wisdom.quote.eventsourcing;

@FunctionalInterface
public interface RetrieveQuote {
  QuoteReducerModel apply(String quoteId) throws Exception;
}
