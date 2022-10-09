package com.wisdom.quote.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wisdom.quote.controller.dto.resp.GetRandomQuoteResp;
import com.wisdom.quote.readmodel.QuoteReadModelRepository;

@RestController
@RequestMapping("/server/{serverId}/quote")
public class QuotesReadController {
	@Autowired
	private QuoteReadModelRepository repo;

	@GetMapping("/random")
	private GetRandomQuoteResp getRandomQuote(@PathVariable String serverId) {
		return new GetRandomQuoteResp(repo.getRandomQuoteInServer(serverId));
	}
}