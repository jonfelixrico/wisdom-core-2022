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
	private QuoteMDBRepository repo;

	private QuoteMDB findById(String id) {
		var result = repo.findById(id);
		if (result.isEmpty()) {
			return null;
		}

		return result.get();
	}

	private static void setRevision(RecordedEvent event, QuoteMDB dbObj) {
		dbObj.setRevision(event.getStreamRevision().getValueUnsigned());
	}

	public void reduce(RecordedEvent event) throws StreamReadException, DatabindException, IOException, UnrecognizedEventTypeException, LaggingRevisionException {
		try {
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
			default:
				throw new UnrecognizedEventTypeException(event.getEventType());
			}
		} catch (AdvancedRevisionException e) {
			LOGGER.debug("Reduce for quote {} skipped due to advanced db copy -- expected {} but found {}",
					e.getQuoteId(), e.getExpectedRevision(), e.getActualRevision());
		}
	}

	private static void checkIfRevisionIsLagging(QuoteMDB dbCopy, RecordedEvent eventToApplyToCopy)
			throws LaggingRevisionException, AdvancedRevisionException {
		var expected = eventToApplyToCopy.getStreamRevision().getValueUnsigned() - 1;
		var actual = dbCopy.getRevision();

		if (expected > actual) {
			throw new LaggingRevisionException(dbCopy.getId(), expected, actual);
		} else if (expected < actual) {
			throw new AdvancedRevisionException(dbCopy.getId(), expected, actual);
		}

		// else, normal revision
	}

	private void reduceReceivedEvent(RecordedEvent event) throws StreamReadException, DatabindException, IOException,
			LaggingRevisionException, AdvancedRevisionException {
		var payload = mapper.readValue(event.getEventData(), QuoteReceivedEvent.class);
		var doc = findById(payload.getQuoteId());

		checkIfRevisionIsLagging(doc, event);

		var newReceive = new Receive(payload.getReceiveId(), payload.getTimestamp(), payload.getUserId(),
				payload.getServerId(), payload.getChannelId(), payload.getMessageId());
		doc.getReceives().add(newReceive);

		setRevision(event, doc);
		repo.save(doc);
	}

	private void reduceStatusDeclaredEvent(RecordedEvent event) throws StreamReadException, DatabindException,
			IOException, LaggingRevisionException, AdvancedRevisionException {
		var data = mapper.readValue(event.getEventData(), QuoteStatusDeclaredEvent.class);
		var doc = findById(data.getQuoteId());

		checkIfRevisionIsLagging(doc, event);

		var newStatus = new StatusDeclaration(data.getStatus(), data.getTimestamp());
		doc.setStatusDeclaration(newStatus);

		setRevision(event, doc);
		repo.save(doc);
	}

	private void reduceSubmittedEvent(RecordedEvent event)
			throws StreamReadException, DatabindException, IOException, AdvancedRevisionException {
		var payload = mapper.readValue(event.getEventData(), QuoteSubmittedEvent.class);

		var foundInDb = findById(payload.getQuoteId());
		if (foundInDb != null) {
			throw new AdvancedRevisionException(payload.getQuoteId(), -1, foundInDb.getRevision());
		}

		var doc = new QuoteMDB(payload.getQuoteId(), payload.getContent(), payload.getAuthorId(),
				payload.getSubmitterId(), payload.getTimestamp(), payload.getExpirationDt(), payload.getServerId(),
				payload.getChannelId(), payload.getMessageId(), List.of(), null, null, payload.getRequiredVoteCount(),
				event.getStreamRevision().getValueUnsigned());
		repo.save(doc);
	}

	private void reduceVotesModifiedEvent(RecordedEvent event) throws StreamReadException, DatabindException,
			IOException, LaggingRevisionException, AdvancedRevisionException {
		var data = mapper.readValue(event.getEventData(), QuoteVotesModifiedEvent.class);
		var doc = findById(data.getQuoteId());

		checkIfRevisionIsLagging(doc, event);

		var votingSession = new VotingSession(data.getTimestamp(), data.getVoterIds());
		doc.setVotingSession(votingSession);

		setRevision(event, doc);
		repo.save(doc);
	}
}
