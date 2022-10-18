package com.wisdom.quote.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.wisdom.quote.readmodel.QuoteReadModel;
import com.wisdom.quote.readmodel.QuoteReadModelRepository;

@RestController
public class PendingQuotesReadController {
	@Autowired
	private QuoteReadModelRepository repo;
	
	@GetMapping("/pending-quote/{quoteId}")
    private QuoteReadModel getPendingQuote(@PathVariable String quoteId) {
        var data = repo.findById(quoteId);
        if (data == null || data.getStatusDeclaration() != null) {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        
        return data;
    }
}
