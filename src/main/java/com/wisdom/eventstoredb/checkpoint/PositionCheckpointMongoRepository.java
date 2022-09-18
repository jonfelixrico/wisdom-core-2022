package com.wisdom.eventstoredb.checkpoint;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PositionCheckpointMongoRepository extends MongoRepository<PositionCheckpointMongoModel, String> {
}
