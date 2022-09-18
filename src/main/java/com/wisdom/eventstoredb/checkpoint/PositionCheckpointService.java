package com.wisdom.eventstoredb.checkpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eventstore.dbclient.Position;

@Service
public class PositionCheckpointService {
	@Autowired
	private PositionCheckpointMongoRepository repo;

	public Position getPosition(String id) {
		var result = repo.findById(id);

		if (result.isEmpty()) {
			return null;
		}

		var data = result.get();
		return new Position(data.getCommit(), data.getPrepare());
	}
	
	private void setPosition(String id, long commit, long prepare) {
		var readResult = repo.findById(id);
		
		var data = readResult.isEmpty() ? new PositionCheckpointMongoModel(id) : readResult.get();
		data.setCommit(commit);
		data.setPrepare(prepare);
		
		repo.save(data);
	}
	
	public void setPosition(String id, Position position) {
		setPosition(id, position.getCommitUnsigned(), position.getPrepareUnsigned());
	}
}
