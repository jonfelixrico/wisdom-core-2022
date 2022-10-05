package com.wisdom.quote.projection;

import org.springframework.data.mongodb.repository.MongoRepository;

interface QuoteDbRepo extends MongoRepository<QuoteDb, String> {

}
