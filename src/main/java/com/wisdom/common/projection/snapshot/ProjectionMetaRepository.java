package com.wisdom.common.projection.snapshot;

import org.springframework.data.mongodb.repository.MongoRepository;

interface ProjectionMetaRepository extends MongoRepository<ProjectionMetaMongoModel, String> {
}
