package com.wisdom.common.readmodel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eventstore.dbclient.Position;

@Service
public class PositionService {
	@Autowired
	PositionMDBRepository repo;

	public Position getPosition(String id) {
		var result = repo.findById(id);
		if (result.isEmpty()) {
			return null;
		}

		var data = result.get();
		return new Position(data.getCommit(), data.getPrepare());
	}

	public void setPosition(String id, Position position) {
		var record = new PositionMDB(id, position.getPrepareUnsigned(), position.getCommitUnsigned());
		repo.save(record);
	}
}
