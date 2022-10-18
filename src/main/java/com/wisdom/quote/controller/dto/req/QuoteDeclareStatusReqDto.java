package com.wisdom.quote.controller.dto.req;

import javax.validation.constraints.NotNull;

import com.wisdom.quote.entity.Status;

public class QuoteDeclareStatusReqDto {
	@NotNull
	private Status status;

	public Status getStatus() {
		return status;
	}

}
