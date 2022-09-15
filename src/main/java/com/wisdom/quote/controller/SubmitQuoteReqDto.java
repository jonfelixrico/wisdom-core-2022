package com.wisdom.quote.controller;

class SubmitQuoteReqDto {
	private String content;
	private String authorId;
	private String submitterId;
	private String expirationDt;

	private String channelId;
	private String messageId;

	public String getContent() {
		return content;
	}

	public String getAuthorId() {
		return authorId;
	}

	public String getSubmitterId() {
		return submitterId;
	}

	public String getExpirationDt() {
		return expirationDt;
	}

	public String getChannelId() {
		return channelId;
	}

	public String getMessageId() {
		return messageId;
	}

}
