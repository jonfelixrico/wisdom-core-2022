package com.wisdom.quote.aggregates;

import java.time.Instant;

class VerdictImpl implements Verdict {
	static Verdict create(VerdictStatus status, Instant verdictDt) {
		return new VerdictImpl(status, verdictDt);
	}

	private Instant verdictDt;
	private VerdictStatus status;

	private VerdictImpl(VerdictStatus status, Instant verdictDt) {
		this.verdictDt = verdictDt;
		this.status = status;
	}

	@Override
	public Instant getVerdictDt() {
		return verdictDt;
	}

	@Override
	public VerdictStatus getStatus() {
		return status;
	}
}
