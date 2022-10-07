package com.wisdom.quote.controller;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import com.wisdom.common.service.TimeService;
import com.wisdom.quote.controller.dto.ReceiveQuoteReqDto;
import com.wisdom.quote.writemodel.QuoteWriteService;

@RestController
@RequestMapping("/server/{serverId}/quote")
public class QuotesWriteController {
	@Autowired
	private QuoteWriteService writeSvc;

	@Autowired
	private TimeService timeSvc;

	@PostMapping("/{quoteId}/receive")
	private void receiveQuote(@PathVariable String serverId, @PathVariable String quoteId,
			@Valid @RequestBody ReceiveQuoteReqDto body) throws Exception {
		var writeModel = writeSvc.get(quoteId);
		if (!writeModel.getServerId().equals(serverId)) {
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
		}

		try {
			writeModel.receive(UUID.randomUUID().toString(), body.getReceiverId(), timeSvc.getCurrentTime(), serverId,
					body.getChannelId(), body.getMessageId());
			writeModel.save();
		} catch (IllegalStateException e) {
			throw new HttpClientErrorException(HttpStatus.FORBIDDEN);
		}
	}
}
