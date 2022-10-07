package com.wisdom.quote.writemodel.projection;

import com.wisdom.quote.entity.QuoteEntity;

class QuoteProjectionImpl extends QuoteProjection {

	QuoteProjectionImpl(QuoteEntity base, long revision) {
		super(base, revision);
	}
}
