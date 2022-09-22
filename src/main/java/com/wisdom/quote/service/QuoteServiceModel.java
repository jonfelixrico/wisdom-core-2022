package com.wisdom.quote.service;

import java.time.Instant;
import java.util.List;

import com.wisdom.quote.projection.Receive;
import com.wisdom.quote.projection.snapshot.QuoteMongoModel;

public class QuoteServiceModel {
	private String id;

	private String content;
	private String authorId;
	private String submitterId;

	private Instant submitDt;
	private Instant approveDt;

	private List<Receive> receives;

	private QuoteServiceModel(String id, String content, String authorId, String submitterId, Instant submitDt,
			Instant approveDt, List<Receive> receives) {
		this.id = id;
		this.content = content;
		this.authorId = authorId;
		this.submitterId = submitterId;
		this.submitDt = submitDt;
		this.approveDt = approveDt;
		this.receives = receives;
	}

	QuoteServiceModel(QuoteMongoModel dbModel) {
		this(dbModel.getId(), dbModel.getContent(), dbModel.getAuthorId(), dbModel.getSubmitterId(),
				dbModel.getSubmitDt(), dbModel.getVerdict().getVerdictDt(), dbModel.getReceives());
	}

	public String getId() {
		return id;
	}

	public String getContent() {
		return content;
	}

	public String getAuthorId() {
		return authorId;
	}

	public String getSubmitterId() {
		return submitterId;
	}

	public Instant getSubmitDt() {
		return submitDt;
	}

	public Instant getApproveDt() {
		return approveDt;
	}

	public List<Receive> getReceives() {
		return receives;
	}

}
