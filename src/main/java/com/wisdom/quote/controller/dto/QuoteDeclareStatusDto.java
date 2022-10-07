package com.wisdom.quote.controller.dto;

import java.time.Instant;

import com.wisdom.quote.entity.Status;
import com.wisdom.quote.entity.StatusDeclaration;

public class QuoteDeclareStatusDto extends StatusDeclaration {
	public QuoteDeclareStatusDto(Status type, Instant timestamp) {
		super(type, timestamp);
		// TODO Auto-generated constructor stub
	}
}
