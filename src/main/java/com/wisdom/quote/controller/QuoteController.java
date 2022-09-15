/**
 * 
 */
package com.wisdom.quote.controller;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.wisdom.quote.writemodel.QuoteWriteModel;

/**
 * @author Felix
 *
 */
@RestController
public class QuoteController {
	@PostMapping("/guild/{serverId}/quote")
	void submitQuote(@RequestBody SubmitQuoteReqDto body, @PathVariable String serverId) throws Exception {
		var quoteId = UUID.randomUUID().toString();
		var createDt = Instant.now();
		var expireDt = createDt.plus(3, ChronoUnit.DAYS);

		QuoteWriteModel.submit(quoteId, body.getContent(), body.getAuthorId(), body.getSubmitterId(), createDt,
				expireDt, serverId, body.getChannelId(), body.getMessageId());
	}
}
