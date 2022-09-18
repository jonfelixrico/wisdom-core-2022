package com.wisdom.quote.projection.snapshot;

import org.springframework.data.mongodb.repository.MongoRepository;

interface QuoteMongoRepository extends MongoRepository<QuoteMongoModel, String> {
}
