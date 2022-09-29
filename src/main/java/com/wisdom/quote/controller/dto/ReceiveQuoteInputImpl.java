package com.wisdom.quote.controller.dto;

import com.wisdom.quote.service.ReceiveQuoteInput;

public class ReceiveQuoteInputImpl implements ReceiveQuoteInput {
	public static ReceiveQuoteInput convertDtoToInput(String serverId, String quoteId, ReceiveQuoteReqDto dto) {
		return new ReceiveQuoteInputImpl(serverId, quoteId, dto.getChannelId(), dto.getMessageId(),
				dto.getReceiverId());
	}

	private String serverId;
	private String quoteId;

	private String channelId;
	private String messageId;

	private String receiverId;

	private ReceiveQuoteInputImpl(String serverId, String quoteId, String channelId, String messageId,
			String receiverId) {
		this.serverId = serverId;
		this.quoteId = quoteId;
		this.channelId = channelId;
		this.messageId = messageId;
		this.receiverId = receiverId;
	}

	public String getServerId() {
		return serverId;
	}

	public String getQuoteId() {
		return quoteId;
	}

	public String getChannelId() {
		return channelId;
	}

	public String getMessageId() {
		return messageId;
	}

	public String getReceiverId() {
		return receiverId;
	}

}
