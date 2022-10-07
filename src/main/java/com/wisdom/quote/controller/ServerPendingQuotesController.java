/**
 * 
 */
package com.wisdom.quote.controller;

import java.io.IOException;
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

import com.wisdom.common.service.TimeService;
import com.wisdom.quote.controller.dto.QuoteDeclareStatusDto;
import com.wisdom.quote.controller.dto.SubmitQuoteReqDto;
import com.wisdom.quote.projection.QuoteProjection;
import com.wisdom.quote.projection.QuoteProjectionService;
import com.wisdom.quote.writemodel.QuoteWriteService;

/**
 * @author Felix
 *
 */
@RestController
@RequestMapping("/server/{serverId}/quote/pending")
public class ServerPendingQuotesController {

	@Autowired
	private QuoteWriteService writeSvc;

	@Autowired
	private TimeService timeSvc;

	@Autowired
	private QuoteProjectionService projSvc;

	@PostMapping
	private Map<String, String> submitQuote(@Valid @RequestBody SubmitQuoteReqDto body) throws Exception {
		var quoteId = UUID.randomUUID().toString();
		var createDt = timeSvc.getCurrentTime();
		var expireDt = createDt.plus(3, ChronoUnit.DAYS);

		var writeModel = writeSvc.create(quoteId, body.getContent(), body.getAuthorId(), body.getSubmitterId(),
				createDt, expireDt, body.getServerId(), body.getChannelId(), body.getMessageId(), 3);
		writeModel.save();

		return Map.of("quoteId", quoteId);
	}

	@PutMapping("/{quoteId}/vote")
	private void setVotes(@PathVariable String quoteId, @PathVariable String serverId,
			@RequestBody List<String> voterIds) throws Exception {
		var writeModel = writeSvc.get(quoteId);
		if (!writeModel.getServerId().equals(serverId)) {
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
		}

		try {
			writeModel.updateVotingSession(voterIds, timeSvc.getCurrentTime());
			writeModel.save();
		} catch (IllegalStateException e) {
			throw new HttpClientErrorException(HttpStatus.FORBIDDEN);
		}
	}

	@GetMapping("/{quoteId}")
	private QuoteProjection getPendingQuote(@PathVariable String serverId, @PathVariable String quoteId)
			throws InterruptedException, ExecutionException, IOException {
		var proj = projSvc.getProjection(quoteId);
		if (!proj.getServerId().equals(serverId) || proj.getStatusDeclaration() != null) {
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
		}

		return proj;
	}

	@GetMapping
	private List<QuoteProjection> getPendingQuotes(@PathVariable String serverId) {
		// TODO restore this
		return null;
	}

	@PostMapping("/{quoteId}/status")
	private void declareStatus(@PathVariable String serverId, @PathVariable String quoteId,
			@RequestBody QuoteDeclareStatusDto body) throws Exception {
		var writeModel = writeSvc.get(quoteId);
		if (!writeModel.getServerId().equals(serverId) || writeModel.getStatusDeclaration() != null) {
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
		}

		try {
			writeModel.declareStatus(body.getStatus(), timeSvc.getCurrentTime());
			writeModel.save();
		} catch (IllegalStateException e) {
			throw new HttpClientErrorException(HttpStatus.FORBIDDEN);
		}
	}
}
