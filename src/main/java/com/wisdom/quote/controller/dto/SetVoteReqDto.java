package com.wisdom.quote.controller.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.wisdom.quote.aggregate.VoteType;

public class SetVoteReqDto {
	// TODO validate enum
	@NotNull
	private VoteType type;

	@NotNull
	@NotBlank
	private String userId;

	public VoteType getType() {
		return type;
	}

	public String getUserId() {
		return userId;
	}
}
