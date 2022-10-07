package com.wisdom.quote.writemodel.projection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wisdom.quote.entity.QuoteEntity;

@Service
class QuoteSnapshotService {
	private static final Logger LOGGER = LoggerFactory.getLogger(QuoteSnapshotService.class);

	@Autowired
	private QuoteSnapshotRepository repo;

	public void save(QuoteEntity data, long revision) {
		var result = repo.findById(data.getId());
		
		if (result.isEmpty()) {
			repo.insert(new QuoteSnapshotDocument(data, revision));
			LOGGER.debug("Inserted snapshot for quote {} with revision {}", data.getId(), revision);
			return;
		}
		
		var fromDb = result.get();
		if (fromDb.getRevision() >= revision) {
			LOGGER.debug("Snapshot skipped for quote {}: revision {} was found (vs {})", data.getId(), fromDb.getRevision(), revision);
			return;
		}
		
		repo.save(new QuoteSnapshotDocument(data, revision));
		LOGGER.debug("Updated the snapshot of quote {} revision {}", data.getId(), revision);
	}

	public QuoteProjection get(String id) {
		var result = repo.findById(id);
		if (result.isEmpty()) {
			LOGGER.debug("Did not find snapshot for quote {}", id);
			return null;
		}

		var data = result.get();
		LOGGER.debug("Retrieved snapshot for quote {} with revision {}", id, data.getRevision());

		return data;
	}
}
