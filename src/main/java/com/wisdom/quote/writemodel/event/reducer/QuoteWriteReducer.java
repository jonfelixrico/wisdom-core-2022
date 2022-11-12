package com.wisdom.quote.writemodel.event.reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.eventstore.dbclient.RecordedEvent;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wisdom.quote.entity.QuoteEntity;
import com.wisdom.quote.entity.Receive;
import com.wisdom.quote.entity.StatusDeclaration;
import com.wisdom.quote.writemodel.event.QuoteReceivedEventV1;
import com.wisdom.quote.writemodel.event.QuoteStatusDeclaredEventV1;
import com.wisdom.quote.writemodel.event.QuoteSubmittedEventV1;
import com.wisdom.quote.writemodel.event.QuoteVoteAddedEventV1;
import com.wisdom.quote.writemodel.event.QuoteVoteRemovedEventV1;

@Service
public class QuoteWriteReducer {
  private static final Logger LOGGER = LoggerFactory.getLogger(QuoteWriteReducer.class);

  @Autowired
  private ObjectMapper mapper;

  /**
   * 
   * @param baseModel
   * @param event
   * @return
   * @throws IOException
   * @throws DatabindException
   * @throws StreamReadException
   */
  public QuoteEntity reduceEvent(QuoteEntity baseModel, RecordedEvent event)
      throws StreamReadException, DatabindException, IOException {

    switch (event.getEventType()) {
      case QuoteSubmittedEventV1.EVENT_TYPE: {
        return reduceQuoteSubmittedEventV1(baseModel, event);
      }

      case QuoteReceivedEventV1.EVENT_TYPE: {
        return reduceQuoteReceivedEventV1(baseModel, event);
      }

      case QuoteStatusDeclaredEventV1.EVENT_TYPE: {
        return reduceQuoteStatusDeclaredEventV1(baseModel, event);
      }

      case QuoteVoteAddedEventV1.EVENT_TYPE: {
        return reduceQuoteVoteAddedEventV1(baseModel, event);
      }

      case QuoteVoteRemovedEventV1.EVENT_TYPE: {
        return reduceQuoteVoteRemovedEventV1(baseModel, event);
      }
    }

    LOGGER.warn("Unregistered Quote class {} detected.", event.getClass());
    return null;
  }

  private QuoteEntity reduceQuoteSubmittedEventV1(QuoteEntity model, RecordedEvent event)
      throws StreamReadException, DatabindException, IOException {
    var parsed = mapper.readValue(event.getEventData(), QuoteSubmittedEventV1.class);
    return new QuoteReducerModel(parsed.getQuoteId(), parsed.getContent(), parsed.getAuthorId(),
        parsed.getSubmitterId(), parsed.getTimestamp(), parsed.getExpirationDt(), parsed.getServerId(),
        parsed.getChannelId(), parsed.getMessageId(), List.of(), null, Map.of(), parsed.getRequiredVoteCount(), false);
  }

  private QuoteEntity reduceQuoteReceivedEventV1(@NonNull QuoteEntity entity, RecordedEvent event)
      throws StreamReadException, DatabindException, IOException {
    var parsed = mapper.readValue(event.getEventData(), QuoteReceivedEventV1.class);
    List<Receive> newReceives = new ArrayList<>();
    newReceives.addAll(entity.getReceives());
    newReceives.add(new Receive(parsed.getReceiveId(), parsed.getTimestamp(), parsed.getUserId(), parsed.getServerId(),
        parsed.getChannelId(), parsed.getMessageId(), false));

    var model = new QuoteReducerModel(entity);
    model.setReceives(newReceives);
    return model;
  }

  private QuoteEntity reduceQuoteStatusDeclaredEventV1(@NonNull QuoteEntity entity, RecordedEvent event)
      throws StreamReadException, DatabindException, IOException {
    var parsed = mapper.readValue(event.getEventData(), QuoteStatusDeclaredEventV1.class);
    var model = new QuoteReducerModel(entity);
    model.setStatusDeclaration(new StatusDeclaration(parsed.getStatus(), parsed.getTimestamp()));
    return model;
  }

  private QuoteEntity reduceQuoteVoteAddedEventV1(@NonNull QuoteEntity entity, RecordedEvent event)
      throws StreamReadException, DatabindException, IOException {
    var parsed = mapper.readValue(event.getEventData(), QuoteVoteAddedEventV1.class);
    var model = new QuoteReducerModel(entity);

    var clone = new HashMap<>(entity.getVotes());
    clone.put(parsed.getUserId(), parsed.getTimestamp());

    model.setVotes(clone);
    return model;
  }

  private QuoteEntity reduceQuoteVoteRemovedEventV1(@NonNull QuoteEntity entity, RecordedEvent event)
      throws StreamReadException, DatabindException, IOException {
    var parsed = mapper.readValue(event.getEventData(), QuoteVoteRemovedEventV1.class);
    var model = new QuoteReducerModel(entity);

    var clone = new HashMap<>(entity.getVotes());

    clone.remove(parsed.getUserId());
    model.setVotes(clone);
    return model;
  }
}
