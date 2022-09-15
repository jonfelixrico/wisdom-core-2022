package com.wisdom.quote.controller;

import java.time.Instant;

public class GetQuoteRespDto {
	private String content;
	private String authorId;
	private Instant submitDt;

	public GetQuoteRespDto(String content, String authorId, Instant submitDt) {
		this.content = content;
		this.authorId = authorId;
		this.submitDt = submitDt;
	}

	public String getContent() {
		return content;
	}

	public String getAuthorId() {
		return authorId;
	}

	public Instant getSubmitDt() {
		return submitDt;
	}

}
