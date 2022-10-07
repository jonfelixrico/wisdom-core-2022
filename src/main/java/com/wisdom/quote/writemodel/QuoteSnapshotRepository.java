package com.wisdom.quote.writemodel;

import org.springframework.data.mongodb.repository.MongoRepository;

interface QuoteSnapshotRepository extends MongoRepository<QuoteSnapshotDocument, String> {

}
