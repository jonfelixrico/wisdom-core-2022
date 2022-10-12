package com.wisdom.quote.writemodel.event.reducer;

import java.util.ArrayList;
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
import com.wisdom.quote.entity.VotingSession;
import com.wisdom.quote.writemodel.event.BaseQuoteEvent;
import com.wisdom.quote.writemodel.event.QuoteReceivedEvent;
import com.wisdom.quote.writemodel.event.QuoteStatusDeclaredEvent;
import com.wisdom.quote.writemodel.event.QuoteSubmittedEvent;
import com.wisdom.quote.writemodel.event.QuoteVotesModifiedEvent;

@Service
public class QuoteEventsReducer {
	private static final Logger LOGGER = LoggerFactory.getLogger(QuoteEventsReducer.class);

	public static final Map<String, Class<? extends BaseQuoteEvent>> EVENT_TYPE_TO_EVENT_CLASS = Map.of(
			QuoteSubmittedEvent.EVENT_TYPE, QuoteSubmittedEvent.class, QuoteReceivedEvent.EVENT_TYPE,
			QuoteReceivedEvent.class, QuoteVotesModifiedEvent.EVENT_TYPE, QuoteVotesModifiedEvent.class,
			QuoteStatusDeclaredEvent.EVENT_TYPE, QuoteStatusDeclaredEvent.class);

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

		if (event instanceof QuoteSubmittedEvent) {
			return reduce(baseModel, (QuoteSubmittedEvent) event);
		}

		if (event instanceof QuoteReceivedEvent) {
			return reduce(baseModel, (QuoteReceivedEvent) event);
		}

		if (event instanceof QuoteStatusDeclaredEvent) {
			return reduce(baseModel, (QuoteStatusDeclaredEvent) event);
		}

		if (event instanceof QuoteVotesModifiedEvent) {
			return reduce(baseModel, (QuoteVotesModifiedEvent) event);
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
	private QuoteEntity reduce(QuoteEntity model, QuoteSubmittedEvent event) {
		return new QuoteReducerModel(event.getQuoteId(), event.getContent(), event.getAuthorId(),
				event.getSubmitterId(), event.getTimestamp(), event.getExpirationDt(), event.getServerId(),
				event.getChannelId(), event.getMessageId(), List.of(), null, null, event.getRequiredVoteCount());
	}

	/**
	 * Processes quote receives
	 * 
	 * @param model
	 * @param event
	 * @return
	 */
	private QuoteEntity reduce(@NonNull QuoteEntity entity, QuoteReceivedEvent event) {
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
	private QuoteEntity reduce(@NonNull QuoteEntity entity, QuoteStatusDeclaredEvent event) {
		var model = new QuoteReducerModel(entity);
		model.setStatusDeclaration(new StatusDeclaration(event.getStatus(), event.getTimestamp()));
		return model;
	}

	private QuoteEntity reduce(@NonNull QuoteEntity entity, QuoteVotesModifiedEvent event) {
		var model = new QuoteReducerModel(entity);
		model.setVotingSession(new VotingSession(event.getTimestamp(), event.getVoterIds()));
		return model;
	}
}
