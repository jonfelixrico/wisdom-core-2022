package com.wisdom.quote.aggregate;

import java.time.Instant;

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
