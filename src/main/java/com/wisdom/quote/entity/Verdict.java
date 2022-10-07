package com.wisdom.quote.entity;

import java.time.Instant;

@Deprecated
public class Verdict {
	private Instant verdictDt;
	private VerdictStatus status;

	public Verdict(VerdictStatus status, Instant verdictDt) {
		this.verdictDt = verdictDt;
		this.status = status;
	}

	public Instant getVerdictDt() {
		return verdictDt;
	}

	public VerdictStatus getStatus() {
		return status;
	}
}
