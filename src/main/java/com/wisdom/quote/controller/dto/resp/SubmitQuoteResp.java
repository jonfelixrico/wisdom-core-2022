package com.wisdom.quote.controller.dto.resp;

public class SubmitQuoteResp {
  private String quoteId;

  public SubmitQuoteResp(String quoteId) {
    this.quoteId = quoteId;
  }

  public String getQuoteId() {
    return quoteId;
  }
}
