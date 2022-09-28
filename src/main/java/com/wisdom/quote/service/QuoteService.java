package com.wisdom.quote.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wisdom.quote.projection.snapshot.QuoteMongoRepository;

@Service
public class QuoteService {
	@Autowired
	private QuoteMongoRepository repo;

	public QuoteServiceModel getRandomQuote(String serverId) {
		var results = repo.getRandomQuote(serverId);
		if (results.size() == 0) {
			return null;
		}
		
		return new QuoteServiceModel(results.get(0));
	}
	
	public void receiveQuote(ReceiveQuoteInput data) {
		// TODO do something
		
		// fetch quote write model by id
		// verify if server checks out
		// verify if verdict checks out
		// add receive
		// return
	}
}
