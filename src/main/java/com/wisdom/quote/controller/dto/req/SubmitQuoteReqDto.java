package com.wisdom.quote.controller.dto.req;

import java.time.Instant;

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
	private Instant expirationDt;

	@NotNull
	@NotBlank
	private String channelId;
	
	@NotNull
	@NotBlank
	private String messageId;
	
	@NotNull
	@NotBlank
	private String serverId;

	public String getServerId() {
		return serverId;
	}

	public String getContent() {
		return content;
	}

	public String getAuthorId() {
		return authorId;
	}

	public String getSubmitterId() {
		return submitterId;
	}

	public Instant getExpirationDt() {
		return expirationDt;
	}

	public String getChannelId() {
		return channelId;
	}

	public String getMessageId() {
		return messageId;
	}

}
