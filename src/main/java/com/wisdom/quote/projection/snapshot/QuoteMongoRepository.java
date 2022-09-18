package com.wisdom.quote.projection.snapshot;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuoteMongoRepository extends MongoRepository<QuoteMongoModel, String> {
}
