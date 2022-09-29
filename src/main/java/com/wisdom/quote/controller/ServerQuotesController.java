package com.wisdom.quote.controller;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wisdom.quote.controller.dto.ReceiveQuoteReqDto;
import com.wisdom.quote.service.QuoteService;
import com.wisdom.quote.service.QuoteServiceModel;

@RestController
@RequestMapping("/server/{serverId}/quote")
public class ServerQuotesController {
	@Autowired
	private QuoteService svc;

	@GetMapping("/random")
	private QuoteServiceModel getRandomQuote(@PathVariable String serverId) {
		return svc.getRandomQuote(serverId);
	}

	@PostMapping("/{quoteId}/receive")
	private void receiveQuote(@PathVariable String serverId, @PathVariable String quoteId,
			@Valid @RequestBody ReceiveQuoteReqDto body) throws InterruptedException, ExecutionException, IOException {
		svc.receiveQuote(quoteId, serverId, body.getReceiverId(), body.getChannelId(), body.getMessageId());
	}
}
