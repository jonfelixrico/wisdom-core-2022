package com.wisdom.quote.aggregate;

import java.time.Instant;

public interface Verdict {
	Instant getVerdictDt();
	VerdictStatus getStatus();
}
