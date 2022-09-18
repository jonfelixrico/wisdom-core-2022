package com.wisdom.common.projection.snapshot;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("projectionMetas")
class ProjectionMetaMongoModel {
	@Id
	private String projectionName;
	private Long lastRevision;

	@PersistenceCreator
	private ProjectionMetaMongoModel(String projectionName, Long lastRevision) {
		this.projectionName = projectionName;
		this.lastRevision = lastRevision;
	}

	public ProjectionMetaMongoModel(String projectionName) {
		this.projectionName = projectionName;
	}

	public String getProjectionName() {
		return projectionName;
	}

	public void setProjectionName(String projectionName) {
		this.projectionName = projectionName;
	}

	public Long getLastRevision() {
		return lastRevision;
	}

	public void setLastRevision(Long lastRevision) {
		this.lastRevision = lastRevision;
	}

}
