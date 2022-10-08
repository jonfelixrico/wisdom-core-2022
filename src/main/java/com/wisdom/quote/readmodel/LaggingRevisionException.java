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
class LaggingRevisionException extends AbnormalRevisionException {
	private static final long serialVersionUID = 9164361454984729153L;

	public LaggingRevisionException(String quoteId, long expectedRevision, long actualRevision) {
		super(quoteId, expectedRevision, actualRevision);
		// TODO Auto-generated constructor stub
	}
}
