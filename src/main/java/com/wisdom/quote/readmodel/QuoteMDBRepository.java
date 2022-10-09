package com.wisdom.quote.readmodel;

import org.springframework.data.mongodb.repository.MongoRepository;

interface QuoteMDBRepository extends MongoRepository<QuoteMDB, String> {

}
