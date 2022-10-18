package com.wisdom.quote.writemodel;

import java.io.IOException;
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
import com.wisdom.quote.entity.QuoteEntity;
import com.wisdom.quote.writemodel.event.reducer.QuoteEventsReducer;

@Service
class QuoteProjectionService {
  private static final Logger LOGGER = LoggerFactory.getLogger(QuoteProjectionService.class);

  @Autowired
  private ESDBClientProvider esdb;

  @Autowired
  private QuoteEventsReducer reducer;

  @Autowired
  private QuoteSnapshotService snapshotRepo;

  @Autowired
  private ObjectMapper mapper;

  public QuoteProjection getProjection(String quoteId)
      throws InterruptedException, ExecutionException, IOException {
    var snapshot = snapshotRepo.get(quoteId);

    try {
      QuoteProjection built;

      if (snapshot != null) {
        built = buildState(quoteId, snapshot.getRevision(), snapshot);
      } else {
        built = buildState(quoteId, null, null);
      }

      snapshotRepo.save(built, built.getRevision());
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

  @SuppressWarnings("null")
  private QuoteProjection buildState(String quoteId, Long fromRevision,
      QuoteEntity baseModel) throws InterruptedException, ExecutionException, IOException, StreamNotFoundException {
    ReadStreamOptions options = ReadStreamOptions.get();
    if (fromRevision == null) {
      options.fromStart();
    } else {
      options.fromRevision(fromRevision);
    }

    var state = baseModel;
    var revision = fromRevision;

    ReadResult results = getEvents(quoteId, fromRevision);
    LOGGER.debug("Found {} events for quote {} starting from revision {}", results.getEvents().size(), quoteId,
        fromRevision);

    for (ResolvedEvent result : results.getEvents()) {
      RecordedEvent event = result.getEvent();
      LOGGER.debug("Reading event type {} for quote {}", event.getEventType(), quoteId);

      var eventClass = reducer.getEventClassFromType(event.getEventType());
      if (eventClass == null) {
        // TODO throw exception
        LOGGER.warn("No event class mapped to event type {}!", event.getEventType());
        continue;
      }

      var eventData = mapper.readValue(event.getEventData(), eventClass);
      state = reducer.reduceEvent(state, eventData);
      revision = event.getStreamRevision().getValueUnsigned();
    }

    return new QuoteProjection(state, revision);
  }
}