/**
 * 
 */
package com.wisdom.quote.controller;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import com.wisdom.quote.controller.dto.SubmitQuoteReqDto;
import com.wisdom.quote.projection.QuoteProjectionModel;
import com.wisdom.quote.projection.QuoteProjectionService;
import com.wisdom.quote.writemodel.QuoteWriteModelRepository;

/**
 * @author Felix
 *
 */
@RestController
@RequestMapping("/quote/pending")
public class QuoteController {
	@Autowired
	QuoteWriteModelRepository writeRepository;

	@Autowired
	QuoteProjectionService projectionService;

	@PostMapping
	private Map<String, String> submitQuote(@Valid @RequestBody SubmitQuoteReqDto body) throws Exception {
		var quoteId = UUID.randomUUID().toString();
		var createDt = Instant.now();
		var expireDt = createDt.plus(3, ChronoUnit.DAYS);

		var writeModel = writeRepository.create(quoteId, body.getContent(), body.getAuthorId(), body.getSubmitterId(),
				createDt, expireDt, body.getServerId(), body.getChannelId(), body.getMessageId());

		writeRepository.saveWriteModel(writeModel);

		return Map.of("quoteId", quoteId);
	}

	@GetMapping("/{id}")
	private QuoteProjectionModel getQuote(@PathVariable String id) throws Exception {
		var data = projectionService.getProjection(id);
		if (data == null) {
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
		}

		return data.getFirst();
	}

	@PutMapping("/{id}/votes")
	private void setVotes(@PathVariable String id, List<String> voterIds)
			throws InterruptedException, ExecutionException, IOException {
		var model = writeRepository.getWriteModel(id);
		model.setVoters(voterIds, Instant.now());
		writeRepository.saveWriteModel(model);
	}
}
