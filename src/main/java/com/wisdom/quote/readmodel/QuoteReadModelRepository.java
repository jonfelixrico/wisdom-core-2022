package com.wisdom.quote.readmodel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wisdom.quote.projection.snapshot.QuoteMongoRepository;

@Service
public class QuoteReadModelRepository {
	@Autowired
	private QuoteMongoRepository repo;
}
