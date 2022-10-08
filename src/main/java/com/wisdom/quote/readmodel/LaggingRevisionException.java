package com.wisdom.quote.readmodel;

/**
 * This being thrown means that the revision of the DB copy of a Quote is behind
 * a relevant event's stream revision.
 * 
 * Normal operation means that the stream revision should be exactly one version
 * ahead of the DB copy. Any further is abnormal operation and we must close the
 * gap immediately.
 * 
 * @author Felix
 *
 */
class LaggingRevisionException extends Exception {
	private static final long serialVersionUID = 9164361454984729153L;

	private String quoteId;
	private long dbRevision;
	private long eventRevision;

	public LaggingRevisionException(String quoteId, long dbRevision, long eventRevision) {
		this.quoteId = quoteId;
		this.dbRevision = dbRevision;
		this.eventRevision = eventRevision;
	}

	public String getQuoteId() {
		return quoteId;
	}

	public long getDbRevision() {
		return dbRevision;
	}

	public long getEventRevision() {
		return eventRevision;
	}

}
