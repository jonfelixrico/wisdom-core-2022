package com.wisdom.quote.writemodel.projection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wisdom.quote.aggregate.Verdict;
import com.wisdom.quote.aggregate.VerdictStatus;
import com.wisdom.quote.writemodel.events.QuoteApprovedBySystemEvent;
import com.wisdom.quote.writemodel.events.QuoteFlaggedAsExpiredBySystemEvent;
import com.wisdom.quote.writemodel.events.QuoteReceivedEvent;
import com.wisdom.quote.writemodel.events.QuoteSubmittedEvent;
import com.wisdom.quote.writemodel.events.QuoteVoteAddedEvent;
import com.wisdom.quote.writemodel.events.QuoteVoteRemovedEvent;

public class QuoteEventsReducer {
	/**
	 * Processes quote submissions
	 * @param model
	 * @param event
	 * @return
	 */
	public static QuoteProjectionModel apply(QuoteProjectionModel model, QuoteSubmittedEvent event) {
		return new QuoteProjectionModel(event.getId(), event.getContent(), event.getAuthorId(), event.getSubmitterId(),
				event.getTimestamp(), event.getExpirationDt(), event.getServerId(), event.getChannelId(),
				event.getMessageId(), Map.of(), List.of(), null);
	}

	/**
	 * Processes quote receives
	 * @param model
	 * @param event
	 * @return
	 */
	public static QuoteProjectionModel apply(QuoteProjectionModel model, QuoteReceivedEvent event) {
		List<Receive> newReceives = new ArrayList<>();
		newReceives.addAll(model.getReceives());
		newReceives.add(new Receive(event.getReceiveId(), event.getTimestamp(), event.getUserId(), event.getServerId(),
				event.getChannelId(), event.getMessageId()));

		return new QuoteProjectionModel(model.getId(), model.getContent(), model.getAuthorId(), model.getSubmitterId(),
				model.getSubmitDt(), model.getExpirationDt(), model.getServerId(), model.getChannelId(),
				model.getMessageId(), model.getVotes(), newReceives, model.getVerdict());
	}
	
	/**
	 * Processes system-triggered expiration-flagging
	 * @param model
	 * @param event
	 * @return
	 */
	public static QuoteProjectionModel apply(QuoteProjectionModel model, QuoteFlaggedAsExpiredBySystemEvent event) {
		Verdict newVerdict = new Verdict(VerdictStatus.EXPIRED, event.getTimestamp());

		return new QuoteProjectionModel(model.getId(), model.getContent(), model.getAuthorId(), model.getSubmitterId(),
				model.getSubmitDt(), model.getExpirationDt(), model.getServerId(), model.getChannelId(),
				model.getMessageId(), model.getVotes(), model.getReceives(), newVerdict);
	}
	
	/**
	 * Processes system-triggered approval
	 * @param model
	 * @param event
	 * @return
	 */
	public static QuoteProjectionModel apply(QuoteProjectionModel model, QuoteApprovedBySystemEvent event) {
		Verdict newVerdict = new Verdict(VerdictStatus.APPROVED, event.getTimestamp());

		return new QuoteProjectionModel(model.getId(), model.getContent(), model.getAuthorId(), model.getSubmitterId(),
				model.getSubmitDt(), model.getExpirationDt(), model.getServerId(), model.getChannelId(),
				model.getMessageId(), model.getVotes(), model.getReceives(), newVerdict);
	}
	
	/**
	 * Processes adding of votes
	 * @param model
	 * @param event
	 * @return
	 */
	public static QuoteProjectionModel apply(QuoteProjectionModel model, QuoteVoteAddedEvent event) {
		Map<String, Vote> newVotes = new HashMap<>();
		newVotes.putAll(model.getVotes());
		newVotes.put(event.getUserId(), new Vote(event.getUserId(), event.getType(), event.getTimestamp()));
		
		return new QuoteProjectionModel(model.getId(), model.getContent(), model.getAuthorId(), model.getSubmitterId(),
				model.getSubmitDt(), model.getExpirationDt(), model.getServerId(), model.getChannelId(),
				model.getMessageId(), newVotes, model.getReceives(), model.getVerdict());
	}
	
	/**
	 * Processes removing of votes
	 * @param model
	 * @param event
	 * @return
	 */
	public static QuoteProjectionModel apply(QuoteProjectionModel model, QuoteVoteRemovedEvent event) {
		Map<String, Vote> newVotes = new HashMap<>();
		newVotes.putAll(model.getVotes());
		newVotes.remove(event.getUserId());
	
		return new QuoteProjectionModel(model.getId(), model.getContent(), model.getAuthorId(), model.getSubmitterId(),
				model.getSubmitDt(), model.getExpirationDt(), model.getServerId(), model.getChannelId(),
				model.getMessageId(), newVotes, model.getReceives(), model.getVerdict());
	}
}
