package com.wisdom.quote.readmodel;

public abstract class AbnormalRevisionException extends Exception {
	private static final long serialVersionUID = -5057058922999844070L;

	private String quoteId;
	private long expectedRevision;
	private long actualRevision;

	public AbnormalRevisionException(String quoteId, long expectedRevision, long actualRevision) {
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

	public long getExpectedRevision() {
		return expectedRevision;
	}

	public long getActualRevision() {
		return actualRevision;
	}

}
