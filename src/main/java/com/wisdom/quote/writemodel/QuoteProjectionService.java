package com.wisdom.quote.writemodel;

import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eventstore.dbclient.ReadResult;
import com.eventstore.dbclient.ReadStreamOptions;
import com.eventstore.dbclient.RecordedEvent;
import com.eventstore.dbclient.ResolvedEvent;
import com.eventstore.dbclient.StreamNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wisdom.eventstoredb.ESDBClientProvider;
import com.wisdom.quote.eventsourcing.QuoteEventsReducer;
import com.wisdom.quote.eventsourcing.QuoteReducerModel;
import com.wisdom.quote.readmodel.QuoteSnapshot;
import com.wisdom.quote.readmodel.QuoteSnapshotRepository;

@Service
class QuoteProjectionService {
  private static final Logger LOGGER = LoggerFactory.getLogger(QuoteProjectionService.class);

  @Autowired
  private ESDBClientProvider esdb;

  @Autowired
  private QuoteSnapshotRepository snapshotRepo;

  @Autowired
  private ObjectMapper mapper;

  private QuoteReducerModel reduceEvent(QuoteReducerModel model, RecordedEvent event) throws Exception {
    var reducer = new QuoteEventsReducer(mapper, quoteId -> model);
    return reducer.reduce(event);
  }

  public QuoteProjection getProjection(String quoteId)
      throws Exception {
    var snapshot = snapshotRepo.findById(quoteId);

    try {
      QuoteProjection built;

      if (snapshot != null) {
        LOGGER.debug("Building state of quote {} from revision {}", quoteId, snapshot.getRevision());
        built = buildStateFromSnapshot(snapshot);
      } else {
        LOGGER.debug("Building state of quote {} from the start", quoteId);
        built = buildStateFromStart(quoteId);
      }

      LOGGER.debug("Built state up to revision {} for quote {}", built.getRevision(), quoteId);
      return built;
    } catch (StreamNotFoundException e) {
      LOGGER.debug("Stream not found for quote {}", quoteId);
      return null;
    }
  }

  private ReadResult getEvents(String quoteId, Long fromRevision)
      throws InterruptedException, ExecutionException, StreamNotFoundException {
    ReadStreamOptions options = ReadStreamOptions.get();
    if (fromRevision != null) {
      options.fromRevision(fromRevision);
    }

    try (var wrapper = esdb.getWrapped()) {
      var client = wrapper.get();

      var streamId = String.format("quote/%s", quoteId);
      return client.readStream(streamId, options).get();
    } catch (ExecutionException e) {
      var cause = e.getCause();
      if (cause instanceof StreamNotFoundException) {
        throw (StreamNotFoundException) cause;
      }

      throw e;
    }
  }

  private QuoteProjection buildStateFromStart(String quoteId) throws Exception, StreamNotFoundException {
    QuoteProjection state = null;

    var results = getEvents(quoteId, null);
    for (ResolvedEvent result : results.getEvents()) {
      RecordedEvent event = result.getEvent();
      LOGGER.debug("Reading event type {} for quote {}", event.getEventType(), quoteId);

      state = new QuoteProjection(reduceEvent(state, event));
    }

    return state;
  }

  private QuoteProjection buildStateFromSnapshot(QuoteSnapshot snapshot)
      throws StreamNotFoundException, Exception {
    QuoteReducerModel state = snapshot;
    var results = getEvents(snapshot.getId(), snapshot.getRevision());
    for (ResolvedEvent result : results.getEvents()) {
      RecordedEvent event = result.getEvent();
      LOGGER.debug("Reading event type {} for quote {}", event.getEventType(), snapshot.getId());

      state = new QuoteProjection(reduceEvent(state, event));
    }

    return new QuoteProjection(state);
  }

}
