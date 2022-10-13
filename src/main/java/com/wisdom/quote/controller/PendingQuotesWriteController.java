/**
 * 
 */
package com.wisdom.quote.controller;

import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.wisdom.common.service.TimeService;
import com.wisdom.quote.controller.dto.req.QuoteAddVoteReqDto;
import com.wisdom.quote.controller.dto.req.QuoteDeclareStatusReqDto;
import com.wisdom.quote.controller.dto.req.SubmitQuoteReqDto;
import com.wisdom.quote.writemodel.QuoteWriteService;

/**
 * @author Felix
 *
 */
@RestController
@RequestMapping("/server/{serverId}/quote/pending")
public class PendingQuotesWriteController {

  @Autowired
  private QuoteWriteService writeSvc;

  @Autowired
  private TimeService timeSvc;

  @PostMapping
  private Map<String, String> submitQuote(@Valid @RequestBody SubmitQuoteReqDto body, @PathVariable String serverId)
      throws Exception {
    var quoteId = UUID.randomUUID().toString();
    var createDt = timeSvc.getCurrentTime();

    // TODO make a service for this -- this is a per-server configuration
    var expireDt = createDt.plus(3, ChronoUnit.DAYS);

    var writeModel = writeSvc.create(quoteId, body.getContent(), body.getAuthorId(), body.getSubmitterId(),
        createDt, expireDt, serverId, body.getChannelId(), body.getMessageId(), 3);
    writeModel.save();

    return Map.of("quoteId", quoteId);
  }

  @PostMapping("/{quoteId}/status")
  private void declareStatus(@PathVariable String serverId, @PathVariable String quoteId,
      @RequestBody QuoteDeclareStatusReqDto body) throws Exception {
    var writeModel = writeSvc.get(quoteId);
    if (!writeModel.getServerId().equals(serverId) || writeModel.getStatusDeclaration() != null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    try {
      writeModel.declareStatus(body.getStatus(), timeSvc.getCurrentTime());
      writeModel.save();
    } catch (IllegalStateException e) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
    }
  }

  @PostMapping("/{quoteId}/vote")
  private void addVote(@PathVariable String quoteId, @PathVariable String serverId,
      @Valid @RequestBody QuoteAddVoteReqDto body) throws Exception {
    var writeModel = writeSvc.get(quoteId);
    if (!writeModel.getServerId().equals(serverId)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    try {
      writeModel.addVote(body.getUserId(), timeSvc.getCurrentTime());
      writeModel.save();
    } catch (IllegalStateException e) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
    }
  }

  @DeleteMapping("/{quoteId}/vote/{userId}")
  private void removeVote(@PathVariable String quoteId, @PathVariable String serverId, @PathVariable String userId)
      throws Exception {
    var writeModel = writeSvc.get(quoteId);
    if (!writeModel.getServerId().equals(serverId)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quote not found.");
    }

    try {
      writeModel.removeVote(userId, timeSvc.getCurrentTime());
      writeModel.save();
    } catch (IllegalStateException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vote note found.");
    }
  }
}
