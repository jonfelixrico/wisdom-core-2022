package com.wisdom.quote.eventsourcing;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eventstore.dbclient.RecordedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wisdom.quote.entity.Receive;
import com.wisdom.quote.entity.StatusDeclaration;
import com.wisdom.quote.eventsourcing.events.QuoteReceivedEventV0;
import com.wisdom.quote.eventsourcing.events.QuoteReceivedEventV1;
import com.wisdom.quote.eventsourcing.events.QuoteStatusDeclaredEventV1;
import com.wisdom.quote.eventsourcing.events.QuoteSubmittedEventV0;
import com.wisdom.quote.eventsourcing.events.QuoteSubmittedEventV1;
import com.wisdom.quote.eventsourcing.events.QuoteVoteAddedEventV0;
import com.wisdom.quote.eventsourcing.events.QuoteVoteAddedEventV1;
import com.wisdom.quote.eventsourcing.events.QuoteVoteRemovedEventV1;
import com.wisdom.quote.readmodel.exception.AdvancedRevisionException;
import com.wisdom.quote.readmodel.exception.LaggingRevisionException;
import com.wisdom.quote.readmodel.exception.UnrecognizedEventTypeException;

public class QuoteEventsReducer {
  private static final Logger LOGGER = LoggerFactory.getLogger(QuoteEventsReducer.class);

  private ObjectMapper mapper;
  private RetrieveQuote retriever;

  public QuoteEventsReducer(ObjectMapper mapper, RetrieveQuote retriever) {
    this.mapper = mapper;
    this.retriever = retriever;
  }

  private MutableQuoteReducerModel dispatchAndReduce(RecordedEvent event) throws Exception {
    switch (event.getEventType()) {
      case QuoteReceivedEventV0.EVENT_TYPE:
        return reduceReceivedEventV0(event);
      case QuoteReceivedEventV1.EVENT_TYPE:
        return reduceReceivedEventV1(event);
      case QuoteStatusDeclaredEventV1.EVENT_TYPE:
        return reduceStatusDeclaredEventV1(event);
      case QuoteSubmittedEventV0.EVENT_TYPE:
        return reduceSubmittedEventV0(event);
      case QuoteSubmittedEventV1.EVENT_TYPE:
        return reduceSubmittedEventV1(event);
      case QuoteVoteRemovedEventV1.EVENT_TYPE:
        return reduceVoteRemovedEventV1(event);
      case QuoteVoteAddedEventV1.EVENT_TYPE:
        return reduceVoteAddedEventV1(event);
      case QuoteVoteAddedEventV0.EVENT_TYPE:
        return reduceVoteAddedEventV0(event);
      default:
        throw new UnrecognizedEventTypeException(event.getEventType());
    }
  }

  public MutableQuoteReducerModel reduce(RecordedEvent event) throws Exception {
    var model = dispatchAndReduce(event);
    model.setRevision(event.getStreamRevision().getValueUnsigned());
    return model;
  }

  /**
   * This method will compare the revision in the model instance vs the event. If
   * a
   * discrepancy is detected -- e.g. theEventRevision -1 != theDbCopyRevision,
   * then we will throw an exception to stop further saves.
   */
  private static void verifyRevision(MutableQuoteReducerModel model, RecordedEvent eventToApply)
      throws LaggingRevisionException, AdvancedRevisionException {
    var expected = eventToApply.getStreamRevision().getValueUnsigned() - 1;
    var actual = model.getRevision();

    if (expected > actual) {
      throw new LaggingRevisionException(model.getId(), expected, actual);
    } else if (expected < actual) {

      throw new AdvancedRevisionException(model.getId(), expected, actual);
    }

    // else, normal revision; no errors thrown
  }

  private MutableQuoteReducerModel retrieve(String quoteId) throws Exception {
    var model = retriever.apply(quoteId);
    return model == null ? null : new MutableQuoteReducerModel(model);
  }

  /*
   * Notes regarding several technical decisions: - We're not following the write
   * model's approach to the reducer methods where methods should contain business
   * logic only because the write model expects each "root reducer" call in
   * succession to contain events for the same quote.
   * 
   * Things are different for the read model since we don't have this kind of
   * assumption. The current main reducer call might belong to quote A but that's
   * not guaranteed to be the same in the next reducer call. This is the reason
   * why we do individual reads and saves per sub-reducer call.
   */

  private MutableQuoteReducerModel reduceReceivedEventV0(RecordedEvent event) throws Exception {
    var payload = mapper.readValue(event.getEventData(), QuoteReceivedEventV0.class);

    var model = retrieve(payload.getQuoteId());
    verifyRevision(model, event);

    var newReceive = new Receive(payload.getReceiveId(), payload.getTimestamp(), payload.getUserId(),
        payload.getServerId(), null, null, true);
    model.getReceives().add(newReceive);

    return model;
  }

  private MutableQuoteReducerModel reduceReceivedEventV1(RecordedEvent event) throws Exception {
    var payload = mapper.readValue(event.getEventData(), QuoteReceivedEventV1.class);
    var model = retrieve(payload.getQuoteId());

    verifyRevision(model, event);

    var newReceive = new Receive(payload.getReceiveId(), payload.getTimestamp(), payload.getUserId(),
        payload.getServerId(), payload.getChannelId(), payload.getMessageId(), false);
    model.getReceives().add(newReceive);

    return model;
  }

  private MutableQuoteReducerModel reduceStatusDeclaredEventV1(RecordedEvent event) throws Exception {
    var data = mapper.readValue(event.getEventData(), QuoteStatusDeclaredEventV1.class);
    var model = retrieve(data.getQuoteId());

    verifyRevision(model, event);

    var newStatus = new StatusDeclaration(data.getStatus(), data.getTimestamp());
    model.setStatusDeclaration(newStatus);

    return model;
  }

  private MutableQuoteReducerModel reduceSubmittedEventV0(RecordedEvent event)
      throws Exception {
    var payload = mapper.readValue(event.getEventData(), QuoteSubmittedEventV0.class);

    var model = retrieve(payload.getQuoteId());
    if (model != null) {
      throw new AdvancedRevisionException(payload.getQuoteId(), -1, model.getRevision());
    }

    return new MutableQuoteReducerModel(payload.getQuoteId(), payload.getContent(), payload.getAuthorId(),
        payload.getSubmitterId(), payload.getTimestamp(), payload.getExpirationDt(), payload.getServerId(),
        null, null, new ArrayList<>(), null, new HashMap<>(), payload.getRequiredVoteCount(), true, null);
  }

  private MutableQuoteReducerModel reduceSubmittedEventV1(RecordedEvent event)
      throws Exception {
    var payload = mapper.readValue(event.getEventData(), QuoteSubmittedEventV1.class);

    var model = retrieve(payload.getQuoteId());
    if (model != null) {
      throw new AdvancedRevisionException(payload.getQuoteId(), -1, model.getRevision());
    }

    return new MutableQuoteReducerModel(payload.getQuoteId(), payload.getContent(), payload.getAuthorId(),
        payload.getSubmitterId(), payload.getTimestamp(), payload.getExpirationDt(), payload.getServerId(),
        payload.getChannelId(), payload.getMessageId(), new ArrayList<>(), null, new HashMap<>(),
        payload.getRequiredVoteCount(),
        false, null);
  }

  private MutableQuoteReducerModel reduceVoteAddedEventV0(RecordedEvent event)
      throws Exception {
    var data = mapper.readValue(event.getEventData(), QuoteVoteAddedEventV0.class);
    var model = retrieve(data.getQuoteId());
    verifyRevision(model, event);

    if (data.getValue() < 1) {
      /*
       * The legacy version used to have downvotes, but that feature is deprecated
       * now.
       * We'll only accept upvotes from the legacy version from now on.
       */
      LOGGER.debug("{}: Ignored vote of user {} for quote {}; reason: legacy downvote event",
          QuoteVoteAddedEventV0.EVENT_TYPE, data.getUserId(), data.getQuoteId());
      return model;
    }

    model.getVotes().put(data.getUserId(), data.getTimestamp());

    return model;
  }

  private MutableQuoteReducerModel reduceVoteAddedEventV1(RecordedEvent event)
      throws Exception {
    var data = mapper.readValue(event.getEventData(), QuoteVoteAddedEventV1.class);
    var model = retrieve(data.getQuoteId());

    verifyRevision(model, event);

    model.getVotes().put(data.getUserId(), data.getTimestamp());

    return model;
  }

  private MutableQuoteReducerModel reduceVoteRemovedEventV1(RecordedEvent event)
      throws Exception {
    var data = mapper.readValue(event.getEventData(), QuoteVoteRemovedEventV1.class);
    var model = retrieve(data.getQuoteId());

    verifyRevision(model, event);

    model.getVotes().remove(data.getUserId());

    return model;
  }

}
