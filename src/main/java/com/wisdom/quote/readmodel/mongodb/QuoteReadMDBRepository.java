package com.wisdom.quote.readmodel.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuoteReadMDBRepository extends MongoRepository<QuoteReadMDB, String> {

}
