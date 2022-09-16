package com.wisdom.quote.controller.dto;

import java.time.Instant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class GetQuoteRespDto {
	@NotNull
	@NotBlank
	private String content;

	@NotNull
	@NotBlank
	private String authorId;

	@NotNull
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
