package com.wisdom.quote.aggregates;

import java.time.Instant;

public interface Verdict {
	Instant getVerdictDt();
	VerdictStatus getStatus();
}
