package com.wisdom.common.readmodel;

public abstract class Position {
	protected String id;
	protected long prepare;
	protected long commit;

	public Position(String id, long prepare, long commit) {
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
