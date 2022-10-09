package com.wisdom.quote.readmodel.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuoteMDBRepository extends MongoRepository<QuoteMDB, String> {

}
