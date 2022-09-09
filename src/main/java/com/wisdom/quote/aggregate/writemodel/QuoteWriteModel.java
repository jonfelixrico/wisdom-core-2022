package com.wisdom.quote.aggregate.writemodel;

import java.math.BigInteger;

import com.wisdom.quote.aggregate.QuoteAggregate;

public class QuoteWriteModel {
	private String quoteId;
	private BigInteger revision;
	private QuoteAggregate aggregate;

	public String getQuoteId() {
		return quoteId;
	}
	
	public BigInteger getRevision() {
		return revision;
	}
}
