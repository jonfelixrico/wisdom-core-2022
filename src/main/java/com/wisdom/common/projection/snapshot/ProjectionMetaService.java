package com.wisdom.common.projection.snapshot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProjectionMetaService {
	@Autowired
	private ProjectionMetaRepository repo;

	@Autowired
	MongoTemplate template;

	public Long getLastRevision(String projectionName) {
		var result = repo.findById(projectionName);
		return result.isEmpty() ? null : result.get().getLastRevision();
	}

	public void setLastRevision(String projectionName, long lastRevision) {
		var result = repo.findById(projectionName);

		// The lastRevision that we're trying to insert is outdated
		if (result.isPresent() && result.get().getLastRevision() >= lastRevision) {
			return;
		}

		ProjectionMetaMongoModel data = result.isPresent() ? result.get() : new ProjectionMetaMongoModel(projectionName);
		data.setLastRevision(lastRevision);
		repo.save(data);
	}
}
