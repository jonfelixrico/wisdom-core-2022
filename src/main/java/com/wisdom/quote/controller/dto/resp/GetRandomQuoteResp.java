package com.wisdom.quote.controller.dto.resp;

import com.wisdom.quote.readmodel.QuoteSnapshot;

public class GetRandomQuoteResp {
  private QuoteSnapshot quote;

  public GetRandomQuoteResp(QuoteSnapshot quote) {
    this.quote = quote;
  }

  protected QuoteSnapshot getQuote() {
    return quote;
  }

}
