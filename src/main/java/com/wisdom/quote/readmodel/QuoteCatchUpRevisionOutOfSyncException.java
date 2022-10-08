package com.wisdom.quote.readmodel;

class QuoteCatchUpRevisionOutOfSyncException extends Exception {
	private static final long serialVersionUID = -2915382339061980692L;

	private String quoteId;
	private Long expectedRevision;
	private Long actualRevision;

	public QuoteCatchUpRevisionOutOfSyncException(String quoteId, Long expectedRevision, Long actualRevision) {
		this.quoteId = quoteId;
		this.expectedRevision = expectedRevision;
		this.actualRevision = actualRevision;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getQuoteId() {
		return quoteId;
	}

	public Long getExpectedRevision() {
		return expectedRevision;
	}

	public Long getActualRevision() {
		return actualRevision;
	}

}
