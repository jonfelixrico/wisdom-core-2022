package com.wisdom.quote.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wisdom.quote.readmodel.QuoteReadModel;
import com.wisdom.quote.readmodel.QuoteReadModelRepository;

@RestController
@RequestMapping("/server/{serverId}/quote/pending")
public class PendingQuotesReadController {
	private QuoteReadModelRepository repo;
	
	@GetMapping
	private List<QuoteReadModel> getPendingQuotes(@PathVariable String serverId) {
		return repo.findPendingQuotesInServer(serverId);
	}
}
