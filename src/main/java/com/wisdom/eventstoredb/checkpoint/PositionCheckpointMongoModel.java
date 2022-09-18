package com.wisdom.eventstoredb.checkpoint;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("positionCheckpoint")
public class PositionCheckpointMongoModel {
	@Id
	private String id;

	private Long commit;
	private Long prepare;

	@PersistenceCreator
	private PositionCheckpointMongoModel(String id, Long commit, Long prepare) {
		this.id = id;
		this.commit = commit;
		this.prepare = prepare;
	}

	public PositionCheckpointMongoModel(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getCommit() {
		return commit;
	}

	public void setCommit(Long commit) {
		this.commit = commit;
	}

	public Long getPrepare() {
		return prepare;
	}

	public void setPrepare(Long prepare) {
		this.prepare = prepare;
	}

}
