package com.wisdom.quote.readmodel;

public class AdvancedRevisionException extends AbnormalRevisionException {

	private static final long serialVersionUID = -474753060882099875L;

	public AdvancedRevisionException(String quoteId, long expectedRevision, long actualRevision) {
		super(quoteId, expectedRevision, actualRevision);
		// TODO Auto-generated constructor stub
	}

}
