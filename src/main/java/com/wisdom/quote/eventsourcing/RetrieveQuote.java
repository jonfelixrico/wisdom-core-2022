package com.wisdom.quote.eventsourcing;

@FunctionalInterface
public interface RetrieveQuote {
  QuoteAggregate apply(String quoteId) throws Exception;
}
