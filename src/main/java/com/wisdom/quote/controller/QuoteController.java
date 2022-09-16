/**
 * 
 */
package com.wisdom.quote.controller;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import com.wisdom.quote.controller.dto.GetQuoteRespDto;
import com.wisdom.quote.controller.dto.RemoveVoteReqDto;
import com.wisdom.quote.controller.dto.SubmitQuoteReqDto;
import com.wisdom.quote.controller.dto.SetVoteReqDto;
import com.wisdom.quote.projection.QuoteProjectionService;
import com.wisdom.quote.projection.Vote;
import com.wisdom.quote.writemodel.QuoteWriteModel;
import com.wisdom.quote.writemodel.QuoteWriteModelRepository;

/**
 * @author Felix
 *
 */
@RestController("/quote/pending")
public class QuoteController {
	@Autowired
	QuoteWriteModelRepository writeRepository;

	@Autowired
	QuoteProjectionService projectionService;

	@PostMapping
	Map<String, String> submitQuote(@RequestBody SubmitQuoteReqDto body) throws Exception {
		var quoteId = UUID.randomUUID().toString();
		var createDt = Instant.now();
		var expireDt = createDt.plus(3, ChronoUnit.DAYS);

		var writeModel = QuoteWriteModel.submit(quoteId, body.getContent(), body.getAuthorId(), body.getSubmitterId(),
				createDt, expireDt, body.getServerId(), body.getChannelId(), body.getMessageId());

		writeRepository.saveWriteModel(writeModel);

		return Map.of("quoteId", quoteId);
	}

	@GetMapping("/{id}")
	GetQuoteRespDto getQuote(@PathVariable String id) throws Exception {
		var data = projectionService.getProjection(id);
		if (data == null) {
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
		}

		var model = data.getFirst();

		return new GetQuoteRespDto(model.getContent(), model.getAuthorId(), model.getSubmitDt());
	}
	
	@PutMapping("/{id}/vote")
	void setVote (@PathVariable String id, @RequestBody SetVoteReqDto body) throws InterruptedException, ExecutionException, IOException {
		var model = writeRepository.getWriteModel(id);
		model.addVote(body.getUserId(), body.getVoteType(), Instant.now());
		writeRepository.saveWriteModel(model);
	}
	
	@DeleteMapping("/{id}/vote")
	void removeVote(@PathVariable String id, @RequestBody RemoveVoteReqDto body) throws InterruptedException, ExecutionException, IOException {
		var model = writeRepository.getWriteModel(id);
		model.removeVote(body.getUserId(), Instant.now());
		writeRepository.saveWriteModel(model);
	}
}
