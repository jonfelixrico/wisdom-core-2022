package com.wisdom.quote.readmodel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eventstore.dbclient.RecordedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wisdom.quote.eventsourcing.QuoteEventsReducer;

@Service
class QuoteReadReducerService {

  QuoteEventsReducer reducer;

  @Autowired
  QuoteSnapshotPersistenceRepository repo;

  private QuoteReadReducerService(QuoteSnapshotPersistenceRepository repo, ObjectMapper mapper) {
    this.reducer = new QuoteEventsReducer(mapper, (String quoteId) -> {
      var result = repo.findById(quoteId);
      if (result.isEmpty()) {
        return null;
      }

      return result.get();
    });
  }

  public void reduce(RecordedEvent event) throws Exception {
    var model = reducer.reduce(event);
    var asDbModel = new QuoteSnapshotPersistence(model);
    repo.save(asDbModel);
  }
}
