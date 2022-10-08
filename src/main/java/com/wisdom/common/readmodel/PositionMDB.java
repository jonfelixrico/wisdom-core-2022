package com.wisdom.common.readmodel;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("position")
class PositionMDB {

	private String id;
	private long prepare;
	private long commit;

	public PositionMDB(String id, long prepare, long commit) {
		this.id = id;
		this.prepare = prepare;
		this.commit = commit;
	}

	public String getId() {
		return id;
	}

	public long getPrepare() {
		return prepare;
	}

	public long getCommit() {
		return commit;
	}

}
