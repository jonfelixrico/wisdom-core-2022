package com.wisdom.quote.readmodel;

import org.springframework.data.mongodb.repository.MongoRepository;

interface QuoteReadMDBRepository extends MongoRepository<QuoteReadMDB, String> {

}
