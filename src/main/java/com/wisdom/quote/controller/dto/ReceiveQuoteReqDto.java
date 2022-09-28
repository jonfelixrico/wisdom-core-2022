package com.wisdom.quote.controller.dto;

public class ReceiveQuoteReqDto {
	private String channelId;
	private String messageId;

	private String quoteId;
	private String receivedBy;

	public String getChannelId() {
		return channelId;
	}

	public String getMessageId() {
		return messageId;
	}

	public String getQuoteId() {
		return quoteId;
	}

	public String getReceivedBy() {
		return receivedBy;
	}
}
