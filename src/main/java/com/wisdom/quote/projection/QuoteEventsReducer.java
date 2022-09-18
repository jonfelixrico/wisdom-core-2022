package com.wisdom.quote.projection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.wisdom.eventstoredb.utils.Event;
import com.wisdom.quote.aggregate.Verdict;
import com.wisdom.quote.aggregate.VerdictStatus;
import com.wisdom.quote.writemodel.events.BaseQuoteEvent;
import com.wisdom.quote.writemodel.events.QuoteApprovedBySystemEvent;
import com.wisdom.quote.writemodel.events.QuoteFlaggedAsExpiredBySystemEvent;
import com.wisdom.quote.writemodel.events.QuoteReceivedEvent;
import com.wisdom.quote.writemodel.events.QuoteSubmittedEvent;
import com.wisdom.quote.writemodel.events.QuoteVotesModifiedEvent;

@Service
class QuoteEventsReducer {
	private static final Logger LOGGER = LoggerFactory.getLogger(QuoteEventsReducer.class);

	public static final Map<String, Class<? extends BaseQuoteEvent>> EVENT_TYPE_TO_EVENT_CLASS = Map.of(
			QuoteSubmittedEvent.EVENT_TYPE, QuoteSubmittedEvent.class, QuoteReceivedEvent.EVENT_TYPE,
			QuoteReceivedEvent.class, QuoteFlaggedAsExpiredBySystemEvent.EVENT_TYPE,
			QuoteFlaggedAsExpiredBySystemEvent.class, QuoteApprovedBySystemEvent.EVENT_TYPE,
			QuoteApprovedBySystemEvent.class, QuoteVotesModifiedEvent.EVENT_TYPE, QuoteVotesModifiedEvent.class);

	@SuppressWarnings("unchecked")
	public static Class<BaseQuoteEvent> getEventClassFromType(String eventType) {
		var value = EVENT_TYPE_TO_EVENT_CLASS.get(eventType);
		if (value == null) {
			return null;
		}

		return (Class<BaseQuoteEvent>) value; // it's guaranteed that only base quote events are in the map so we can suppress the warning
	}

	/**
	 * 
	 * @param baseModel
	 * @param event
	 * @return
	 */
	public QuoteProjectionModel reduce(QuoteProjectionModel baseModel, Event event) {
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

		if (event instanceof QuoteFlaggedAsExpiredBySystemEvent) {
			return reduce(baseModel, (QuoteFlaggedAsExpiredBySystemEvent) event);
		}

		if (event instanceof QuoteApprovedBySystemEvent) {
			return reduce(baseModel, (QuoteApprovedBySystemEvent) event);
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
	private QuoteProjectionModelImpl reduce(QuoteProjectionModel model, QuoteSubmittedEvent event) {
		return new QuoteProjectionModelImpl(event.getId(), event.getContent(), event.getAuthorId(),
				event.getSubmitterId(), event.getTimestamp(), event.getExpirationDt(), event.getServerId(),
				event.getChannelId(), event.getMessageId(), List.of(), List.of(), null, event.getRequiredVoteCount());
	}

	/**
	 * Processes quote receives
	 * 
	 * @param model
	 * @param event
	 * @return
	 */
	private QuoteProjectionModelImpl reduce(@NonNull QuoteProjectionModel model, QuoteReceivedEvent event) {
		List<Receive> newReceives = new ArrayList<>();
		newReceives.addAll(model.getReceives());
		newReceives.add(new Receive(event.getReceiveId(), event.getTimestamp(), event.getUserId(), event.getServerId(),
				event.getChannelId(), event.getMessageId()));

		return new QuoteProjectionModelImpl(model.getId(), model.getContent(), model.getAuthorId(),
				model.getSubmitterId(), model.getSubmitDt(), model.getExpirationDt(), model.getServerId(),
				model.getChannelId(), model.getMessageId(), model.getVoterIds(), newReceives, model.getVerdict(),
				model.getRequiredVoteCount());
	}

	/**
	 * Processes system-triggered expiration-flagging
	 * 
	 * @param model
	 * @param event
	 * @return
	 */
	private QuoteProjectionModelImpl reduce(@NonNull QuoteProjectionModel model,
			QuoteFlaggedAsExpiredBySystemEvent event) {
		Verdict newVerdict = new Verdict(VerdictStatus.EXPIRED, event.getTimestamp());

		return new QuoteProjectionModelImpl(model.getId(), model.getContent(), model.getAuthorId(),
				model.getSubmitterId(), model.getSubmitDt(), model.getExpirationDt(), model.getServerId(),
				model.getChannelId(), model.getMessageId(), model.getVoterIds(), model.getReceives(), newVerdict,
				model.getRequiredVoteCount());
	}

	/**
	 * Processes system-triggered approval
	 * 
	 * @param model
	 * @param event
	 * @return
	 */
	private QuoteProjectionModelImpl reduce(@NonNull QuoteProjectionModel model, QuoteApprovedBySystemEvent event) {
		Verdict newVerdict = new Verdict(VerdictStatus.APPROVED, event.getTimestamp());

		return new QuoteProjectionModelImpl(model.getId(), model.getContent(), model.getAuthorId(),
				model.getSubmitterId(), model.getSubmitDt(), model.getExpirationDt(), model.getServerId(),
				model.getChannelId(), model.getMessageId(), model.getVoterIds(), model.getReceives(), newVerdict,
				model.getRequiredVoteCount());
	}

	private QuoteProjectionModelImpl reduce(@NonNull QuoteProjectionModel model, QuoteVotesModifiedEvent event) {
		return new QuoteProjectionModelImpl(model.getId(), model.getContent(), model.getAuthorId(),
				model.getSubmitterId(), model.getSubmitDt(), model.getExpirationDt(), model.getServerId(),
				model.getChannelId(), model.getMessageId(), event.getVoterIds(), model.getReceives(),
				model.getVerdict(), model.getRequiredVoteCount());
	}
}
