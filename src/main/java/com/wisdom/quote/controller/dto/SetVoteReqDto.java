package com.wisdom.quote.controller.dto;

import com.wisdom.quote.aggregate.VoteType;

public class SetVoteReqDto {
	private VoteType voteType;
	private String userId;

	public VoteType getVoteType() {
		return voteType;
	}

	public String getUserId() {
		return userId;
	}
}
