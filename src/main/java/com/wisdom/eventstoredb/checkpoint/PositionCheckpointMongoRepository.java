package com.wisdom.eventstoredb.checkpoint;

import org.springframework.data.mongodb.repository.MongoRepository;

interface PositionCheckpointMongoRepository extends MongoRepository<PositionCheckpointMongoModel, String> {
}
