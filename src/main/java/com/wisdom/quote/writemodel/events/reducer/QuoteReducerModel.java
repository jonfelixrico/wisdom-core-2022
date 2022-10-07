package com.wisdom.quote.writemodel.events.reducer;

import java.time.Instant;
import java.util.List;

import com.wisdom.quote.entity.QuoteEntity;
import com.wisdom.quote.entity.Receive;
import com.wisdom.quote.entity.StatusDeclaration;
import com.wisdom.quote.entity.VotingSession;

class QuoteReducerModel extends QuoteEntity {

	public QuoteReducerModel(String id, String content, String authorId, String submitterId, Instant submitDt,
			Instant expirationDt, String serverId, String channelId, String messageId, List<Receive> receives,
			StatusDeclaration status, VotingSession votingSession, Integer requiredVoteCount) {
		super(id, content, authorId, submitterId, submitDt, expirationDt, serverId, channelId, messageId, receives,
				status, votingSession, requiredVoteCount);
		// TODO Auto-generated constructor stub
	}

	public QuoteReducerModel(QuoteEntity entity) {
		this(entity.getId(), entity.getContent(), entity.getAuthorId(), entity.getSubmitterId(), entity.getSubmitDt(),
				entity.getExpirationDt(), entity.getServerId(), entity.getChannelId(), entity.getMessageId(),
				entity.getReceives(), entity.getStatusDeclaration(), entity.getVotingSession(),
				entity.getRequiredVoteCount());
	}

	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub
		super.setId(id);
	}

	@Override
	public void setContent(String content) {
		// TODO Auto-generated method stub
		super.setContent(content);
	}

	@Override
	public void setAuthorId(String authorId) {
		// TODO Auto-generated method stub
		super.setAuthorId(authorId);
	}

	@Override
	public void setSubmitterId(String submitterId) {
		// TODO Auto-generated method stub
		super.setSubmitterId(submitterId);
	}

	@Override
	public void setSubmitDt(Instant submitDt) {
		// TODO Auto-generated method stub
		super.setSubmitDt(submitDt);
	}

	@Override
	public void setExpirationDt(Instant expirationDt) {
		// TODO Auto-generated method stub
		super.setExpirationDt(expirationDt);
	}

	@Override
	public void setServerId(String serverId) {
		// TODO Auto-generated method stub
		super.setServerId(serverId);
	}

	@Override
	public void setChannelId(String channelId) {
		// TODO Auto-generated method stub
		super.setChannelId(channelId);
	}

	@Override
	protected void setMessageId(String messageId) {
		// TODO Auto-generated method stub
		super.setMessageId(messageId);
	}

	@Override
	public void setReceives(List<Receive> receives) {
		// TODO Auto-generated method stub
		super.setReceives(receives);
	}

	@Override
	public void setVotingSession(VotingSession votingSession) {
		// TODO Auto-generated method stub
		super.setVotingSession(votingSession);
	}

	@Override
	public void setRequiredVoteCount(Integer requiredVoteCount) {
		// TODO Auto-generated method stub
		super.setRequiredVoteCount(requiredVoteCount);
	}

	@Override
	public void setStatusDeclaration(StatusDeclaration status) {
		// TODO Auto-generated method stub
		super.setStatusDeclaration(status);
	}

}
