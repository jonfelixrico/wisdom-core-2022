package com.wisdom.quote.readmodel;

import java.time.Instant;
import java.util.List;

import com.wisdom.quote.aggregate.Verdict;
import com.wisdom.quote.projection.Receive;

public class QuoteReadModel {
	private String id;

	private String content;
	private String authorId;
	private String submitterId;

	private Instant submitDt;

	private List<Receive> receives;
	private Verdict verdict;

	QuoteReadModel(String id, String content, String authorId, String submitterId, Instant submitDt,
			List<Receive> receives, Verdict verdict) {
		this.id = id;
		this.content = content;
		this.authorId = authorId;
		this.submitterId = submitterId;
		this.submitDt = submitDt;
		this.receives = receives;
		this.verdict = verdict;
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

	public List<Receive> getReceives() {
		return receives;
	}

	public Verdict getVerdict() {
		return verdict;
	}

}
