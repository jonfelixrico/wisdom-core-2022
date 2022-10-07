package com.wisdom.quote.readmodel;

import org.springframework.data.mongodb.repository.MongoRepository;

interface QuoteDocumentRepository extends MongoRepository<QuoteDocument, String> {

}
