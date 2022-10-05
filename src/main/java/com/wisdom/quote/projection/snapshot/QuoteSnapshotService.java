package com.wisdom.quote.projection.snapshot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import com.wisdom.quote.entity.QuoteEntity;

@Service
public class QuoteSnapshotService {
	private static final Logger LOGGER = LoggerFactory.getLogger(QuoteSnapshotService.class);

	@Autowired
	private QuoteDbRepo repo;

	@Autowired
	private MongoTemplate template;

	public void save(QuoteEntity baseQuoteProjectionModel, long revision) {
		var dbModel = new QuoteDb(baseQuoteProjectionModel, revision);
		if (!repo.existsById(baseQuoteProjectionModel.getId())) {
			repo.insert(dbModel);
			LOGGER.debug("Created snapshot of quote {} revision {}", baseQuoteProjectionModel.getId(), revision);
			return;
		}

		Query query = new Query()
				.addCriteria(Criteria.where("id").is(baseQuoteProjectionModel.getId()).and("revision").lt(revision));
		var result = template.findAndReplace(query, dbModel);

		if (result != null) {
			LOGGER.debug("Updated the snapshot of quote {} revision {}", baseQuoteProjectionModel.getId(), revision);
		}
	}

	public Pair<QuoteEntity, Long> get(String id) {
		var result = repo.findById(id);
		if (result.isEmpty()) {
			LOGGER.debug("Did not find snapshot for quote {}", id);
			return null;
		}

		var data = result.get();
		LOGGER.debug("Retrieved snapshot for quote {} with revision {}", id, data.getRevision());

		return Pair.of(data, data.getRevision());
	}
}
