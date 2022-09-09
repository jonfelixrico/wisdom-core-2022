package com.wisdom.quote.aggregate;

import java.time.Instant;

class VerdictImpl implements Verdict {
	private Instant verdictDt;
	private VerdictStatus status;

	public VerdictImpl(VerdictStatus status, Instant verdictDt) {
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
