package com.wisdom.quote.controller.dto.resp;

import com.wisdom.quote.readmodel.QuoteReadModel;

public class GetRandomQuoteResp {
	private QuoteReadModel quote;

	public GetRandomQuoteResp(QuoteReadModel quote) {
		this.quote = quote;
	}

	protected QuoteReadModel getQuote() {
		return quote;
	}

}
