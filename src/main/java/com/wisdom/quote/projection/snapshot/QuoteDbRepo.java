package com.wisdom.quote.projection.snapshot;

import org.springframework.data.mongodb.repository.MongoRepository;

interface QuoteDbRepo extends MongoRepository<QuoteDb, String> {

}
