package com.wisdom.quote.controller;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.wisdom.common.service.TimeService;
import com.wisdom.quote.controller.dto.req.ReceiveQuoteReqDto;
import com.wisdom.quote.writemodel.QuoteWriteModelRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/quote/{quoteId}")
public class QuotesWriteController {
  @Autowired
  private QuoteWriteModelRepository writeSvc;

  @Autowired
  private TimeService timeSvc;

  @Operation(operationId = "Add receive", summary = "Add a receive to a quote")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "404", description = "Quote not found"),
      @ApiResponse()
  })
  @PostMapping("/receive")
  private void receiveQuote(@PathVariable String quoteId,
      @Valid @RequestBody ReceiveQuoteReqDto body) throws Exception {
    var writeModel = writeSvc.get(quoteId);
    if (writeModel == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    try {
      writeModel.receive(UUID.randomUUID().toString(), body.getReceiverId(), timeSvc.getCurrentTime(),
          writeModel.getServerId(),
          body.getChannelId(), body.getMessageId());
      writeModel.save();
    } catch (IllegalStateException e) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
    }
  }
}
