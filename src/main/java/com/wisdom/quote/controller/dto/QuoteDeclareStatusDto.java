package com.wisdom.quote.controller.dto;

import javax.validation.constraints.NotNull;

import com.wisdom.quote.entity.Status;

public class QuoteDeclareStatusDto {
	@NotNull
	private Status status;

	public Status getStatus() {
		return status;
	}

}
