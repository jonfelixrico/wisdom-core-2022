package com.wisdom.common.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

@Service
class TimeServiceImpl extends TimeService {

	@Override
	public Instant getCurrentTime() {
		return Instant.now();
	}
}
