package com.wisdom.quote.controller.dto;

import com.wisdom.quote.aggregate.VoteType;

public class SetVoteReqDto {
	private VoteType type;
	private String userId;

	public VoteType getType() {
		return type;
	}

	public String getUserId() {
		return userId;
	}
}
