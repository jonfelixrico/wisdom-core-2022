package com.wisdom.quote.controller.dto.req;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ReceiveQuoteReqDto {
	@NotNull
	@NotBlank
	private String channelId;
	
	// TODO do something about missing message ids
	private String messageId;
	
	@NotNull
	@NotBlank
	private String receiverId;

	public String getChannelId() {
		return channelId;
	}

	public String getMessageId() {
		return messageId;
	}

	public String getReceiverId() {
		return receiverId;
	}
}
