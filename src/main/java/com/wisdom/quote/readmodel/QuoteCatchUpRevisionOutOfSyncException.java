package com.wisdom.quote.readmodel;

class QuoteCatchUpRevisionOutOfSyncException extends Exception {
	private static final long serialVersionUID = -2915382339061980692L;

	private String quoteId;
	private long expectedRevision;
	private long actualRevision;

	public QuoteCatchUpRevisionOutOfSyncException(String quoteId, long expectedRevision, long actualRevision) {
		this.quoteId = quoteId;
		this.expectedRevision = expectedRevision;
		this.actualRevision = actualRevision;
	}

	public String getQuoteId() {
		return quoteId;
	}

	public long getExpectedRevision() {
		return expectedRevision;
	}

	public long getActualRevision() {
		return actualRevision;
	}

}
