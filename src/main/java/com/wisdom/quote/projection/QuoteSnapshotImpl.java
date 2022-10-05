package com.wisdom.quote.projection;

import com.wisdom.quote.entity.QuoteEntity;

public class QuoteSnapshotImpl extends QuoteSnapshot {

	QuoteSnapshotImpl(QuoteEntity base, long revision) {
		super(base, revision);
	}
}
