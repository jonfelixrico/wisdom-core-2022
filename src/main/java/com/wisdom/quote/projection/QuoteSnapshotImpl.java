package com.wisdom.quote.projection;

import com.wisdom.quote.entity.QuoteEntity;
import com.wisdom.quote.projection.snapshot.QuoteSnapshot;

public class QuoteSnapshotImpl extends QuoteSnapshot {

	QuoteSnapshotImpl(QuoteEntity base, long revision) {
		super(base, revision);
	}
}
