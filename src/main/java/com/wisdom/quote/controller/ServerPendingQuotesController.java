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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wisdom.quote.controller.dto.SubmitQuoteReqDto;
import com.wisdom.quote.readmodel.PendingQuoteReadModel;
import com.wisdom.quote.readmodel.PendingQuoteReadModelRepository;
import com.wisdom.quote.writemodel.QuoteWriteModelRepository;

/**
 * @author Felix
 *
 */
@RestController
@RequestMapping("/server/{serverId}/quote/pending")
public class ServerPendingQuotesController {
	@Autowired
	QuoteWriteModelRepository writeRepository;

	@Autowired
	PendingQuoteReadModelRepository readRepo;

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

	@PutMapping("/{id}/vote")
	private void setVotes(@PathVariable String id, @RequestBody List<String> voterIds)
			throws InterruptedException, ExecutionException, IOException {
		var model = writeRepository.getWriteModel(id);
		model.setVoters(voterIds, Instant.now());
		writeRepository.saveWriteModel(model);
	}

	@GetMapping("/{id}")
	private PendingQuoteReadModel getPendingQuote(@PathVariable String serverId, @PathVariable String id) {
		return this.readRepo.getPendingQuote(id, serverId);
	}

	@GetMapping
	private List<PendingQuoteReadModel> getPendingQuotes(@PathVariable String serverId) {
		return this.readRepo.getPendingQuotes(serverId);
	}

}
