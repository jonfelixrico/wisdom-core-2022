package com.wisdom.quote.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wisdom.quote.projection.snapshot.QuoteMongoRepository;

@Service
public class QuoteService {
	@Autowired
	private QuoteMongoRepository repo;
}
