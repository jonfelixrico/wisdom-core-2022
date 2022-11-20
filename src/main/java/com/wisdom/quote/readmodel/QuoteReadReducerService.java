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
  QuoteReadMDBRepository repo;
  
  private QuoteReadReducerService (QuoteReadMDBRepository repo, ObjectMapper mapper)  {
    this.reducer = new QuoteEventsReducer(mapper, (String quoteId) -> {
      var result = repo.findById(null);
      if (result.isPresent()) {
        return null;
      }
      
      return result.get();
    });
  }
  
  public void reduce(RecordedEvent event) throws Exception {
    var model = reducer.reduce(event);
    var asDbModel = QuoteReadDBModel.fromModel(model);
    repo.save(asDbModel);
  }
}
