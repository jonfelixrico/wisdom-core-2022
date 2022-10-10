package com.wisdom.quote.readmodel;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.eventstore.dbclient.ReadStreamOptions;
import com.eventstore.dbclient.ResolvedEvent;
import com.eventstore.dbclient.SubscribeToAllOptions;
import com.eventstore.dbclient.Subscription;
import com.eventstore.dbclient.SubscriptionFilter;
import com.eventstore.dbclient.SubscriptionListener;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.wisdom.common.readmodel.PositionService;
import com.wisdom.eventstoredb.ESDBClientFactory;
import com.wisdom.quote.readmodel.exception.LaggingRevisionException;
import com.wisdom.quote.readmodel.exception.UnrecognizedEventTypeException;

@Service
class QuoteReadCatchUp {
  private static final Logger LOGGER = LoggerFactory.getLogger(QuoteReadCatchUp.class);

  private static final String POSITION_ID = "quote-readmodel";

  @Autowired
  private QuoteReadReducer reducer;

  @Autowired
  private ESDBClientFactory esdb;

  @Autowired
  private PositionService posSvc;

  private void processLagCatchUpEvent(ResolvedEvent event)
      throws StreamReadException, DatabindException, IOException {
    var recordedEvt = event.getEvent();
    try {
      reducer.reduce(recordedEvt);
    } catch (LaggingRevisionException e) {
      // impossible to have lagging models at this point since we're especially
      // catching-up
    } catch (UnrecognizedEventTypeException e) {
      // suppressed, we don't care about this in this context
    }
  }

  private void catchUpLaggingModel(LaggingRevisionException cause, ResolvedEvent event) {
    try {
      var startRevision = cause.getActualRevision() + 1;
      var maxCount = event.getEvent().getStreamRevision().getValueUnsigned() - startRevision + 1; // + 1 for
                                                                                                  // inclusiveness
      var options = ReadStreamOptions.get().fromRevision(cause.getActualRevision() + 1);

      try (var wrapper = esdb.getInstance()) {
        var client = wrapper.get();
        var results = client.readStream(event.getEvent().getStreamId(), maxCount, options).get();
        for (ResolvedEvent inner : results.getEvents()) {
          processLagCatchUpEvent(inner);
        }
      }
    } catch (Exception e) {
      LOGGER.error("Error encountered while trying to catch-up a lagging model", e);
    }
  }

  private void processEvent(ResolvedEvent event) throws StreamReadException, DatabindException, IOException {
    var recordedEvt = event.getEvent();
    try {
      reducer.reduce(recordedEvt);
    } catch (LaggingRevisionException e) {
      catchUpLaggingModel(e, event);
    } catch (UnrecognizedEventTypeException e) {
      LOGGER.debug("Skipped unrecognized event type {}", e.getEventType());
    }
  }

  private SubscriptionListener getListener() {
    return new SubscriptionListener() {
      @Override
      public void onEvent(Subscription subscription, ResolvedEvent event) {
        try {
          processEvent(event);
        } catch (Exception e) {
          LOGGER.error("Unexpected exception encountered while processing catch-up event", e);
        }

        try {
          posSvc.setPosition(POSITION_ID, event.getEvent().getPosition());
        } catch (Exception e) {
          LOGGER.error("Unexpected exception encountered while saving position data", e);
        }
      }
    };
  }

  private SubscribeToAllOptions getOptions() {
    SubscriptionFilter filter = SubscriptionFilter.newBuilder().withEventTypePrefix("QUOTE_").build();
    var options = SubscribeToAllOptions.get().filter(filter);

    var position = posSvc.getPosition(POSITION_ID);
    if (position != null) {
      LOGGER.info("Starting catch-up from prepare {}, commit {}", position.getPrepareUnsigned(),
          position.getCommitUnsigned());
      options.fromPosition(null);
    }

    return options;
  }

  @EventListener
  private void startCatchUp(ApplicationStartedEvent ctx) throws InterruptedException, ExecutionException {
    LOGGER.info("Starting catch-up...");
    esdb.getInstance().get().subscribeToAll(getListener(), getOptions());
  }
}
