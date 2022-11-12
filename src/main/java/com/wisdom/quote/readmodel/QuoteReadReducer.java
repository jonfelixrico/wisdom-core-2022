package com.wisdom.quote.readmodel;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eventstore.dbclient.RecordedEvent;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wisdom.quote.entity.Receive;
import com.wisdom.quote.entity.StatusDeclaration;
import com.wisdom.quote.readmodel.exception.AdvancedRevisionException;
import com.wisdom.quote.readmodel.exception.LaggingRevisionException;
import com.wisdom.quote.readmodel.exception.UnrecognizedEventTypeException;
import com.wisdom.quote.writemodel.event.QuoteReceivedEventV0;
import com.wisdom.quote.writemodel.event.QuoteReceivedEventV1;
import com.wisdom.quote.writemodel.event.QuoteStatusDeclaredEventV1;
import com.wisdom.quote.writemodel.event.QuoteSubmittedEventV0;
import com.wisdom.quote.writemodel.event.QuoteSubmittedEventV1;
import com.wisdom.quote.writemodel.event.QuoteVoteAddedEventV0;
import com.wisdom.quote.writemodel.event.QuoteVoteAddedEventV1;
import com.wisdom.quote.writemodel.event.QuoteVoteRemovedEventV1;

@Service
class QuoteReadReducer {
  private static final Logger LOGGER = LoggerFactory.getLogger(QuoteReadReducer.class);

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private QuoteReadMDBRepository repo;

  private QuoteReadMDB findById(String id) {
    var result = repo.findById(id);
    if (result.isEmpty()) {
      return null;
    }

    return result.get();
  }

  private static void setRevision(RecordedEvent event, QuoteReadMDB dbObj) {
    dbObj.setRevision(event.getStreamRevision().getValueUnsigned());
  }

  public void reduce(RecordedEvent event) throws StreamReadException, DatabindException, IOException,
      UnrecognizedEventTypeException, LaggingRevisionException {
    try {
      switch (event.getEventType()) {
        case QuoteReceivedEventV0.EVENT_TYPE:
          reduceReceivedEventV0(event);
          return;
        case QuoteReceivedEventV1.EVENT_TYPE:
          reduceReceivedEventV1(event);
          return;
        case QuoteStatusDeclaredEventV1.EVENT_TYPE:
          reduceStatusDeclaredEventV1(event);
          return;
        case QuoteSubmittedEventV0.EVENT_TYPE:
          reduceSubmittedEventV0(event);
          return;
        case QuoteSubmittedEventV1.EVENT_TYPE:
          reduceSubmittedEventV1(event);
          return;
        case QuoteVoteRemovedEventV1.EVENT_TYPE:
          reduceVoteRemovedEventV1(event);
          return;
        case QuoteVoteAddedEventV1.EVENT_TYPE:
          reduceVoteAddedEventV1(event);
          return;
        case QuoteVoteAddedEventV0.EVENT_TYPE:
          reduceVoteAddedEventV0(event);
          return;
        default:
          throw new UnrecognizedEventTypeException(event.getEventType());
      }
    } catch (AdvancedRevisionException e) {
      LOGGER.debug("Reduce for quote {} was skipped due to advanced db copy -- expected {} but found {}",
          e.getQuoteId(), e.getExpectedRevision(), e.getActualRevision());
    }
  }

  /**
   * This method will compare the revision in the DB copy and the event. If a
   * discrepancy is detected -- e.g. theEventRevision -1 != theDbCopyRevision,
   * then we will throw an exception to stop further saves.
   */
  private static void verifyRevision(QuoteReadMDB dbCopy, RecordedEvent eventToApplyToCopy)
      throws LaggingRevisionException, AdvancedRevisionException {
    var expected = eventToApplyToCopy.getStreamRevision().getValueUnsigned() - 1;
    var actual = dbCopy.getRevision();

    if (expected > actual) {
      /*
       * This kind of error being thrown means that we have to "catch up" the lagging
       * model.
       */
      throw new LaggingRevisionException(dbCopy.getId(), expected, actual);
    } else if (expected < actual) {
      /*
       * This kind, on the other hand, is just thrown as an easy way to interrupt the
       * reducer for the sake of avoiding incorrectly mutating the state with an
       * outdated
       * event.
       * 
       * This kind of error is harmless -- advanced db copies are weird but do not
       * need any action
       * from us.
       */
      throw new AdvancedRevisionException(dbCopy.getId(), expected, actual);
    }

    // else, normal revision
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

  private void reduceReceivedEventV0(RecordedEvent event) throws StreamReadException, DatabindException, IOException,
      LaggingRevisionException, AdvancedRevisionException {
    var payload = mapper.readValue(event.getEventData(), QuoteReceivedEventV0.class);
    var doc = findById(payload.getQuoteId());

    verifyRevision(doc, event);

    var newReceive = new Receive(payload.getReceiveId(), payload.getTimestamp(), payload.getUserId(),
        payload.getServerId(), null, null);
    doc.getReceives().add(newReceive);

    setRevision(event, doc);
    repo.save(doc);
  }

  private void reduceReceivedEventV1(RecordedEvent event) throws StreamReadException, DatabindException, IOException,
      LaggingRevisionException, AdvancedRevisionException {
    var payload = mapper.readValue(event.getEventData(), QuoteReceivedEventV1.class);
    var doc = findById(payload.getQuoteId());

    verifyRevision(doc, event);

    var newReceive = new Receive(payload.getReceiveId(), payload.getTimestamp(), payload.getUserId(),
        payload.getServerId(), payload.getChannelId(), payload.getMessageId());
    doc.getReceives().add(newReceive);

    setRevision(event, doc);
    repo.save(doc);
  }

  private void reduceStatusDeclaredEventV1(RecordedEvent event) throws StreamReadException, DatabindException,
      IOException, LaggingRevisionException, AdvancedRevisionException {
    var data = mapper.readValue(event.getEventData(), QuoteStatusDeclaredEventV1.class);
    var doc = findById(data.getQuoteId());

    verifyRevision(doc, event);

    var newStatus = new StatusDeclaration(data.getStatus(), data.getTimestamp());
    doc.setStatusDeclaration(newStatus);

    setRevision(event, doc);
    repo.save(doc);
  }

  private void reduceSubmittedEventV0(RecordedEvent event)
      throws StreamReadException, DatabindException, IOException, AdvancedRevisionException {
    var payload = mapper.readValue(event.getEventData(), QuoteSubmittedEventV0.class);

    var foundInDb = findById(payload.getQuoteId());
    if (foundInDb != null) {
      throw new AdvancedRevisionException(payload.getQuoteId(), -1, foundInDb.getRevision());
    }

    var doc = new QuoteReadMDB(payload.getQuoteId(), payload.getContent(), payload.getAuthorId(),
        payload.getSubmitterId(), payload.getTimestamp(), payload.getExpirationDt(), payload.getServerId(),
        null, null, List.of(), null, Map.of(), payload.getRequiredVoteCount(),
        event.getStreamRevision().getValueUnsigned());
    repo.save(doc);
  }

  private void reduceSubmittedEventV1(RecordedEvent event)
      throws StreamReadException, DatabindException, IOException, AdvancedRevisionException {
    var payload = mapper.readValue(event.getEventData(), QuoteSubmittedEventV1.class);

    var foundInDb = findById(payload.getQuoteId());
    if (foundInDb != null) {
      throw new AdvancedRevisionException(payload.getQuoteId(), -1, foundInDb.getRevision());
    }

    var doc = new QuoteReadMDB(payload.getQuoteId(), payload.getContent(), payload.getAuthorId(),
        payload.getSubmitterId(), payload.getTimestamp(), payload.getExpirationDt(), payload.getServerId(),
        payload.getChannelId(), payload.getMessageId(), List.of(), null, Map.of(), payload.getRequiredVoteCount(),
        event.getStreamRevision().getValueUnsigned());
    repo.save(doc);
  }

  private void reduceVoteAddedEventV0(RecordedEvent event)
      throws StreamReadException, DatabindException, IOException, LaggingRevisionException, AdvancedRevisionException {
    var data = mapper.readValue(event.getEventData(), QuoteVoteAddedEventV0.class);

    if (data.getValue() < 1) {
      /*
       * The legacy version used to have downvotes, but that feature is deprecated
       * now.
       * We'll only accept upvotes from the legacy version from now on.
       */
      return;
    }

    var doc = findById(data.getQuoteId());

    verifyRevision(doc, event);

    var clone = new HashMap<>(doc.getVotes());
    clone.put(data.getUserId(), data.getTimestamp());
    doc.setVotes(clone);

    setRevision(event, doc);
    repo.save(doc);
  }

  private void reduceVoteAddedEventV1(RecordedEvent event)
      throws StreamReadException, DatabindException, IOException, LaggingRevisionException, AdvancedRevisionException {
    var data = mapper.readValue(event.getEventData(), QuoteVoteAddedEventV1.class);
    var doc = findById(data.getQuoteId());

    verifyRevision(doc, event);

    var clone = new HashMap<>(doc.getVotes());
    clone.put(data.getUserId(), data.getTimestamp());
    doc.setVotes(clone);

    setRevision(event, doc);
    repo.save(doc);
  }

  private void reduceVoteRemovedEventV1(RecordedEvent event)
      throws StreamReadException, DatabindException, IOException, LaggingRevisionException, AdvancedRevisionException {
    var data = mapper.readValue(event.getEventData(), QuoteVoteRemovedEventV1.class);
    var doc = findById(data.getQuoteId());

    verifyRevision(doc, event);

    var clone = new HashMap<>(doc.getVotes());
    clone.remove(data.getUserId());
    doc.setVotes(clone);

    setRevision(event, doc);
    repo.save(doc);
  }

}
