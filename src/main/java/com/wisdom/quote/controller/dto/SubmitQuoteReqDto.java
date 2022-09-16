package com.wisdom.quote.controller.dto;

import java.time.Instant;

public class SubmitQuoteReqDto {
	private String content;
	private String authorId;
	private String submitterId;
	private Instant expirationDt;

	private String channelId;
	private String messageId;
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
