package com.wisdom.quote.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuoteMongoRepository extends MongoRepository<QuoteMongoModel, String> {
}
