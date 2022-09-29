package com.wisdom.quote.controller.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ReceiveQuoteReqDto {
	@NotNull
	@NotBlank
	private String channelId;
	
	@NotNull
	@NotBlank
	private String messageId;

	@NotNull
	@NotBlank
	private String quoteId;
	
	@NotNull
	@NotBlank
	private String receiverId;

	public String getChannelId() {
		return channelId;
	}

	public String getMessageId() {
		return messageId;
	}

	public String getQuoteId() {
		return quoteId;
	}

	public String getReceiverId() {
		return receiverId;
	}
}
