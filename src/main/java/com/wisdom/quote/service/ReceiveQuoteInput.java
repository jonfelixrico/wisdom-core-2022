package com.wisdom.quote.service;

public interface ReceiveQuoteInput {
	public String getServerId();
	public String getQuoteId();
	
	public String getChannelId();
	public String getMessageId();
	
	public String getReceiverId();
}
