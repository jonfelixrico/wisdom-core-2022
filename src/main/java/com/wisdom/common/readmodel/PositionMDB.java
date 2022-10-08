package com.wisdom.common.readmodel;

import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("position")
class PositionMDB extends Position {

	@PersistenceCreator
	public PositionMDB(String id, long prepare, long commit) {
		super(id, prepare, commit);
		// TODO Auto-generated constructor stub
	}

	public void setPrepare(long prepare) {
		this.prepare = prepare;
	}

	public void setCommit(long commit) {
		this.commit = commit;
	}

}
