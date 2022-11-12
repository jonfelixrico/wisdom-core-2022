package com.wisdom.quote.writemodel.event.reducer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.wisdom.eventstoredb.utils.Event;
import com.wisdom.quote.entity.QuoteEntity;
import com.wisdom.quote.entity.Receive;
import com.wisdom.quote.entity.StatusDeclaration;
import com.wisdom.quote.writemodel.event.BaseQuoteEvent;
import com.wisdom.quote.writemodel.event.QuoteReceivedEventV1;
import com.wisdom.quote.writemodel.event.QuoteStatusDeclaredEventV1;
import com.wisdom.quote.writemodel.event.QuoteSubmittedEventV1;
import com.wisdom.quote.writemodel.event.QuoteVoteAddedEventV1;
import com.wisdom.quote.writemodel.event.QuoteVoteRemovedEventV1;

@Service
public class QuoteEventsReducer {
  private static final Logger LOGGER = LoggerFactory.getLogger(QuoteEventsReducer.class);

  public static final Map<String, Class<? extends BaseQuoteEvent>> EVENT_TYPE_TO_EVENT_CLASS = Map.of(
      QuoteSubmittedEventV1.EVENT_TYPE, QuoteSubmittedEventV1.class, QuoteReceivedEventV1.EVENT_TYPE,
      QuoteReceivedEventV1.class,
      QuoteStatusDeclaredEventV1.EVENT_TYPE, QuoteStatusDeclaredEventV1.class, QuoteVoteAddedEventV1.EVENT_TYPE,
      QuoteVoteAddedEventV1.class, QuoteVoteRemovedEventV1.EVENT_TYPE, QuoteVoteRemovedEventV1.class);

  @SuppressWarnings("unchecked")
  public Class<BaseQuoteEvent> getEventClassFromType(String eventType) {
    var value = EVENT_TYPE_TO_EVENT_CLASS.get(eventType);
    if (value == null) {
      return null;
    }

    return (Class<BaseQuoteEvent>) value; // it's guaranteed that only base quote events are in the map so we can
                                          // suppress the warning
  }

  /**
   * 
   * @param baseModel
   * @param event
   * @return
   */
  public QuoteEntity reduceEvent(QuoteEntity baseModel, Event event) {
    if (!(event instanceof BaseQuoteEvent)) {
      /*
       * We're only concerned with events under the quote aggregate. Such events are
       * expected to extend BaseQuoteEvent, hence, we can use an `instanceof` check to
       * quickly terminate non-quote events.
       */
      return null;
    }

    if (event instanceof QuoteSubmittedEventV1) {
      return reduce(baseModel, (QuoteSubmittedEventV1) event);
    }

    if (event instanceof QuoteReceivedEventV1) {
      return reduce(baseModel, (QuoteReceivedEventV1) event);
    }

    if (event instanceof QuoteStatusDeclaredEventV1) {
      return reduce(baseModel, (QuoteStatusDeclaredEventV1) event);
    }

    if (event instanceof QuoteVoteAddedEventV1) {
      return reduce(baseModel, (QuoteVoteAddedEventV1) event);
    }

    if (event instanceof QuoteVoteRemovedEventV1) {
      return reduce(baseModel, (QuoteVoteRemovedEventV1) event);
    }

    LOGGER.warn("Unregistered Quote class {} detected.", event.getClass());
    return null;
  }

  /**
   * Processes quote submissions
   * 
   * @param model
   * @param event
   * @return
   */
  private QuoteEntity reduce(QuoteEntity model, QuoteSubmittedEventV1 event) {
    return new QuoteReducerModel(event.getQuoteId(), event.getContent(), event.getAuthorId(),
        event.getSubmitterId(), event.getTimestamp(), event.getExpirationDt(), event.getServerId(),
        event.getChannelId(), event.getMessageId(), List.of(), null, Map.of(), event.getRequiredVoteCount());
  }

  /**
   * Processes quote receives
   * 
   * @param model
   * @param event
   * @return
   */
  private QuoteEntity reduce(@NonNull QuoteEntity entity, QuoteReceivedEventV1 event) {
    List<Receive> newReceives = new ArrayList<>();
    newReceives.addAll(entity.getReceives());
    newReceives.add(new Receive(event.getReceiveId(), event.getTimestamp(), event.getUserId(), event.getServerId(),
        event.getChannelId(), event.getMessageId()));

    var model = new QuoteReducerModel(entity);
    model.setReceives(newReceives);
    return model;
  }

  /**
   * Processes system-triggered expiration-flagging
   * 
   * @param entity
   * @param event
   * @return
   */
  private QuoteEntity reduce(@NonNull QuoteEntity entity, QuoteStatusDeclaredEventV1 event) {
    var model = new QuoteReducerModel(entity);
    model.setStatusDeclaration(new StatusDeclaration(event.getStatus(), event.getTimestamp()));
    return model;
  }

  private QuoteEntity reduce(@NonNull QuoteEntity entity, QuoteVoteAddedEventV1 event) {
    var model = new QuoteReducerModel(entity);

    var clone = new HashMap<>(entity.getVotes());
    clone.put(event.getUserId(), event.getTimestamp());

    model.setVotes(clone);
    return model;
  }

  private QuoteEntity reduce(@NonNull QuoteEntity entity, QuoteVoteRemovedEventV1 event) {
    var model = new QuoteReducerModel(entity);

    var clone = new HashMap<>(entity.getVotes());

    clone.remove(event.getUserId());
    model.setVotes(clone);
    return model;
  }
}
