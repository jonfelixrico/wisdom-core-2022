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
import com.wisdom.quote.service.PendingQuoteReadModel;
import com.wisdom.quote.service.PendingQuoteService;
import com.wisdom.quote.writemodel.QuoteWriteModelRepository;

/**
 * @author Felix
 *
 */
@RestController
@RequestMapping("/server/{serverId}/quote/pending")
public class ServerPendingQuotesController {
	@Autowired
	private QuoteWriteModelRepository writeRepo;

	@Autowired
	private PendingQuoteService pendingQuoteSvc;

	@PostMapping
	private Map<String, String> submitQuote(@Valid @RequestBody SubmitQuoteReqDto body) throws Exception {
		var quoteId = UUID.randomUUID().toString();
		var createDt = Instant.now();
		var expireDt = createDt.plus(3, ChronoUnit.DAYS);

		var writeModel = writeRepo.create(quoteId, body.getContent(), body.getAuthorId(), body.getSubmitterId(),
				createDt, expireDt, body.getServerId(), body.getChannelId(), body.getMessageId());

		writeRepo.saveWriteModel(writeModel);

		return Map.of("quoteId", quoteId);
	}

	@PutMapping("/{id}/vote")
	private void setVotes(@PathVariable String id, @RequestBody List<String> voterIds)
			throws InterruptedException, ExecutionException, IOException {
		var model = writeRepo.getWriteModel(id);
		model.setVoters(voterIds, Instant.now());
		writeRepo.saveWriteModel(model);
	}

	@GetMapping("/{id}")
	private PendingQuoteReadModel getPendingQuote(@PathVariable String serverId, @PathVariable String id) {
		return this.pendingQuoteSvc.getPendingQuote(id, serverId);
	}

	@GetMapping
	private List<PendingQuoteReadModel> getPendingQuotes(@PathVariable String serverId) {
		return this.pendingQuoteSvc.getPendingQuotes(serverId);
	}

}
