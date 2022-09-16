package com.wisdom.quote.controller.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class RemoveVoteReqDto {
	@NotNull
	@NotBlank
	private String userId;

	public String getUserId() {
		return userId;
	}

}
