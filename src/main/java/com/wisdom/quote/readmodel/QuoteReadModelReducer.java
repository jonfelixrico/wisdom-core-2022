package com.wisdom.quote.readmodel;

import java.io.IOException;
import java.util.List;

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
import com.wisdom.quote.entity.VotingSession;
import com.wisdom.quote.writemodel.event.QuoteReceivedEvent;
import com.wisdom.quote.writemodel.event.QuoteStatusDeclaredEvent;
import com.wisdom.quote.writemodel.event.QuoteSubmittedEvent;
import com.wisdom.quote.writemodel.event.QuoteVotesModifiedEvent;

@Service
class QuoteReadModelReducer {
	private static final Logger LOGGER = LoggerFactory.getLogger(QuoteReadModelReducer.class);

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private QuoteDocumentRepository repo;

	private QuoteDocument findById(String id) {
		var result = repo.findById(id);
		if (result.isEmpty()) {
			return null;
		}

		return result.get();
	}

	private static void setRevision(RecordedEvent event, QuoteDocument dbObj) {
		dbObj.setRevision(event.getStreamRevision().getValueUnsigned());
	}

	public void reduce(RecordedEvent event)
			throws StreamReadException, DatabindException, IOException, QuoteCatchUpRevisionOutOfSyncException {
		switch (event.getEventType()) {
		case QuoteReceivedEvent.EVENT_TYPE:
			reduceReceivedEvent(event);
			break;
		case QuoteStatusDeclaredEvent.EVENT_TYPE:
			reduceStatusDeclaredEvent(event);
			break;
		case QuoteSubmittedEvent.EVENT_TYPE:
			reduceSubmittedEvent(event);
			break;
		case QuoteVotesModifiedEvent.EVENT_TYPE:
			reduceVotesModifiedEvent(event);
			break;

		}
	}

	private static void checkIfRevisionIsInSequence(String quoteId, RecordedEvent event, long docRevision)
			throws QuoteCatchUpRevisionOutOfSyncException {
		var expectedRevision = event.getStreamRevision().getValueUnsigned() - 1;
		if (expectedRevision != docRevision) {
			return;
		}

		LOGGER.warn("Detected catch-up desync for quote {}; expected revision {} but received {} instead", quoteId,
				expectedRevision, docRevision);
		throw new QuoteCatchUpRevisionOutOfSyncException(quoteId, expectedRevision, docRevision);
	}

	private static void checkIfRevisionIsInSequence(RecordedEvent event, QuoteDocument doc)
			throws QuoteCatchUpRevisionOutOfSyncException {
		checkIfRevisionIsInSequence(doc.getId(), event, doc.getRevision());
	}

	private void reduceReceivedEvent(RecordedEvent event)
			throws StreamReadException, DatabindException, IOException, QuoteCatchUpRevisionOutOfSyncException {
		var payload = mapper.readValue(event.getEventData(), QuoteReceivedEvent.class);
		var doc = findById(payload.getQuoteId());

		checkIfRevisionIsInSequence(event, doc);

		var newReceive = new Receive(payload.getReceiveId(), payload.getTimestamp(), payload.getUserId(),
				payload.getServerId(), payload.getChannelId(), payload.getMessageId());
		doc.getReceives().add(newReceive);
		setRevision(event, doc);

		repo.save(doc);
	}

	private void reduceStatusDeclaredEvent(RecordedEvent event)
			throws StreamReadException, DatabindException, IOException, QuoteCatchUpRevisionOutOfSyncException {
		var data = mapper.readValue(event.getEventData(), QuoteStatusDeclaredEvent.class);
		var doc = findById(data.getQuoteId());

		checkIfRevisionIsInSequence(event, doc);

		setRevision(event, doc);

		var newStatus = new StatusDeclaration(data.getStatus(), data.getTimestamp());
		doc.setStatusDeclaration(newStatus);
		repo.save(doc);
	}

	private void reduceSubmittedEvent(RecordedEvent event)
			throws StreamReadException, DatabindException, IOException, QuoteCatchUpRevisionOutOfSyncException {
		var payload = mapper.readValue(event.getEventData(), QuoteSubmittedEvent.class);

		var foundInDb = findById(payload.getQuoteId());
		if (foundInDb != null) {
			LOGGER.warn(
					"Detected catch-up desync for quote {}; expected no document to be found yet but found document with revision {} instead",
					payload.getQuoteId(), foundInDb.getRevision());
			throw new QuoteCatchUpRevisionOutOfSyncException(payload.getQuoteId(), null, foundInDb.getRevision());
		}

		var doc = new QuoteDocument(payload.getQuoteId(), payload.getContent(), payload.getAuthorId(),
				payload.getSubmitterId(), payload.getTimestamp(), payload.getExpirationDt(), payload.getServerId(),
				payload.getChannelId(), payload.getMessageId(), List.of(), null, null, payload.getRequiredVoteCount(),
				event.getStreamRevision().getValueUnsigned());
		repo.save(doc);
	}

	private void reduceVotesModifiedEvent(RecordedEvent event)
			throws StreamReadException, DatabindException, IOException, QuoteCatchUpRevisionOutOfSyncException {
		var data = mapper.readValue(event.getEventData(), QuoteVotesModifiedEvent.class);
		var doc = findById(data.getQuoteId());

		checkIfRevisionIsInSequence(event, doc);

		var votingSession = new VotingSession(data.getTimestamp(), data.getVoterIds());
		doc.setVotingSession(votingSession);
		setRevision(event, doc);
		repo.save(doc);
	}
}
