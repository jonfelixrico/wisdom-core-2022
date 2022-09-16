package com.wisdom.quote.projection;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.wisdom.common.writemodel.Event;
import com.wisdom.quote.aggregate.Verdict;
import com.wisdom.quote.aggregate.VerdictStatus;
import com.wisdom.quote.writemodel.events.BaseQuoteEvent;
import com.wisdom.quote.writemodel.events.QuoteApprovedBySystemEvent;
import com.wisdom.quote.writemodel.events.QuoteFlaggedAsExpiredBySystemEvent;
import com.wisdom.quote.writemodel.events.QuoteReceivedEvent;
import com.wisdom.quote.writemodel.events.QuoteSubmittedEvent;
import com.wisdom.quote.writemodel.events.QuoteVotesModifiedEvent;

@Service
public class QuoteEventsReducer {
	private static final Logger LOGGER = LoggerFactory.getLogger(QuoteEventsReducer.class);
	
	/**
	 * 
	 * @param model
	 * @param event
	 * @return
	 */
	public QuoteProjectionModel reduce(QuoteProjectionModel model, Event event) {
		if (!(event instanceof BaseQuoteEvent)) {
			/*
			 * We're only concerned with events under the quote aggregate.
			 * Such events are expected to extend BaseQuoteEvent, hence, we can use an `instanceof`
			 * check to quickly terminate non-quote events.
			 */
			return null;
		}
		
		if (event instanceof QuoteSubmittedEvent) {
			return reduce(model, (QuoteSubmittedEvent) event);
		}
		
		if (event instanceof QuoteReceivedEvent) {
			return reduce(model, (QuoteReceivedEvent) event);
		}
		
		if (event instanceof QuoteFlaggedAsExpiredBySystemEvent) {
			return reduce(model, (QuoteFlaggedAsExpiredBySystemEvent) event);
		}
		
		if (event instanceof QuoteApprovedBySystemEvent) {
			return reduce(model, (QuoteApprovedBySystemEvent) event);
		}
		
		if (event instanceof QuoteVotesModifiedEvent) {
			return reduce(model, (QuoteVotesModifiedEvent) event);
		}
		
		LOGGER.warn("Unregistered Quote class {} detected.", event.getClass());
		return null;
	}
	
	/**
	 * Processes quote submissions
	 * @param model
	 * @param event
	 * @return
	 */
	private QuoteProjectionModel reduce(QuoteProjectionModel model, QuoteSubmittedEvent event) {
		return new QuoteProjectionModel(event.getId(), event.getContent(), event.getAuthorId(), event.getSubmitterId(),
				event.getTimestamp(), event.getExpirationDt(), event.getServerId(), event.getChannelId(),
				event.getMessageId(), List.of(), List.of(), null, model.getRequiredVoteCount());
	}

	/**
	 * Processes quote receives
	 * @param model
	 * @param event
	 * @return
	 */
	private QuoteProjectionModel reduce(@NonNull QuoteProjectionModel model, QuoteReceivedEvent event) {
		List<Receive> newReceives = new ArrayList<>();
		newReceives.addAll(model.getReceives());
		newReceives.add(new Receive(event.getReceiveId(), event.getTimestamp(), event.getUserId(), event.getServerId(),
				event.getChannelId(), event.getMessageId()));

		return new QuoteProjectionModel(model.getId(), model.getContent(), model.getAuthorId(), model.getSubmitterId(),
				model.getSubmitDt(), model.getExpirationDt(), model.getServerId(), model.getChannelId(),
				model.getMessageId(), model.getVoterIds(), newReceives, model.getVerdict(), model.getRequiredVoteCount());
	}
	
	/**
	 * Processes system-triggered expiration-flagging
	 * @param model
	 * @param event
	 * @return
	 */
	private QuoteProjectionModel reduce(@NonNull QuoteProjectionModel model, QuoteFlaggedAsExpiredBySystemEvent event) {
		Verdict newVerdict = new Verdict(VerdictStatus.EXPIRED, event.getTimestamp());

		return new QuoteProjectionModel(model.getId(), model.getContent(), model.getAuthorId(), model.getSubmitterId(),
				model.getSubmitDt(), model.getExpirationDt(), model.getServerId(), model.getChannelId(),
				model.getMessageId(), model.getVoterIds(), model.getReceives(), newVerdict, model.getRequiredVoteCount());
	}
	
	/**
	 * Processes system-triggered approval
	 * @param model
	 * @param event
	 * @return
	 */
	private QuoteProjectionModel reduce(@NonNull QuoteProjectionModel model, QuoteApprovedBySystemEvent event) {
		Verdict newVerdict = new Verdict(VerdictStatus.APPROVED, event.getTimestamp());

		return new QuoteProjectionModel(model.getId(), model.getContent(), model.getAuthorId(), model.getSubmitterId(),
				model.getSubmitDt(), model.getExpirationDt(), model.getServerId(), model.getChannelId(),
				model.getMessageId(), model.getVoterIds(), model.getReceives(), newVerdict, model.getRequiredVoteCount());
	}
	
	private QuoteProjectionModel reduce(@NonNull QuoteProjectionModel model, QuoteVotesModifiedEvent event) {
		return new QuoteProjectionModel(model.getId(), model.getContent(), model.getAuthorId(), model.getSubmitterId(),
				model.getSubmitDt(), model.getExpirationDt(), model.getServerId(), model.getChannelId(),
				model.getMessageId(), event.getVoterIds(), model.getReceives(), model.getVerdict(), model.getRequiredVoteCount());
	}
}
