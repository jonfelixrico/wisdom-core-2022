package com.wisdom.quote.controller.dto.req;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SubmitQuoteReqDto {
	@NotNull
	@NotBlank
	private String content;
	
	@NotNull
	@NotBlank
	private String authorId;
	
	@NotNull
	@NotBlank
	private String submitterId;

	@NotNull
	@NotBlank
	private String channelId;
	
	// TODO do something about null message ids
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

	public String getChannelId() {
		return channelId;
	}

	public String getMessageId() {
		return messageId;
	}

}
