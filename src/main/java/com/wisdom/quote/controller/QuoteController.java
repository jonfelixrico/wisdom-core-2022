/**
 * 
 */
package com.wisdom.quote.controller;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import com.wisdom.quote.controller.dto.SubmitQuoteReqDto;
import com.wisdom.quote.projection.QuoteProjectionService;
import com.wisdom.quote.writemodel.QuoteWriteModel;
import com.wisdom.quote.writemodel.QuoteWriteModelRepository;

/**
 * @author Felix
 *
 */
@RestController
public class QuoteController {
	@Autowired
	QuoteWriteModelRepository writeRepository;

	@Autowired
	QuoteProjectionService projectionService;

	@PostMapping("/server/{serverId}/quote")
	String submitQuote(@RequestBody SubmitQuoteReqDto body, @PathVariable String serverId) throws Exception {
		var quoteId = UUID.randomUUID().toString();
		var createDt = Instant.now();
		var expireDt = createDt.plus(3, ChronoUnit.DAYS);

		var writeModel = QuoteWriteModel.submit(quoteId, body.getContent(), body.getAuthorId(), body.getSubmitterId(),
				createDt, expireDt, serverId, body.getChannelId(), body.getMessageId());

		writeRepository.saveWriteModel(writeModel);
		
		return quoteId;
	}

	@GetMapping("/quote/{quoteId}")
	GetQuoteRespDto getQuote(@PathVariable String quoteId) throws Exception {
		var data = projectionService.getProjection(quoteId);
		if (data == null) {
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
		}

		var model = data.getFirst();

		return new GetQuoteRespDto(model.getContent(), model.getAuthorId(), model.getSubmitDt());
	}
}
