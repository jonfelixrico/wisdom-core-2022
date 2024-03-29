/**
 * 
 */
package com.wisdom.quote.controller;

import java.time.temporal.ChronoUnit;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.wisdom.common.service.TimeService;
import com.wisdom.quote.controller.dto.req.SubmitQuoteReqDto;
import com.wisdom.quote.controller.dto.resp.SubmitQuoteResp;
import com.wisdom.quote.writemodel.QuoteWriteModelRepository;

import io.swagger.v3.oas.annotations.Operation;

/**
 * @author Felix
 *
 */
@RestController
public class ServerPendingQuotesWriteController {

  @Autowired
  private QuoteWriteModelRepository writeSvc;

  @Autowired
  private TimeService timeSvc;

  @Operation(operationId = "submitQuote", summary = "Submit quote")
  @PostMapping("/server/{serverId}/pending-quote")
  private SubmitQuoteResp submitQuote(@Valid @RequestBody SubmitQuoteReqDto body, @PathVariable String serverId)
      throws Exception {
    var quoteId = UUID.randomUUID().toString();
    var createDt = timeSvc.getCurrentTime();

    // TODO make a service for this -- this is a per-server configuration
    var expireDt = createDt.plus(3, ChronoUnit.DAYS);

    var writeModel = writeSvc.create(quoteId, body.getContent(), body.getAuthorId(), body.getSubmitterId(),
        createDt, expireDt, serverId, body.getChannelId(), body.getMessageId(), 3);
    writeModel.save();

    return new SubmitQuoteResp(quoteId);
  }
}
