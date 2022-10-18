/**
 * 
 */
package com.wisdom.quote.controller;

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
import com.wisdom.quote.writemodel.QuoteWriteService;

/**
 * @author Felix
 *
 */
@RestController
@RequestMapping("/pending-quote/{quoteId}")
public class PendingQuotesWriteController {

  @Autowired
  private QuoteWriteService writeSvc;

  @Autowired
  private TimeService timeSvc;

  @PostMapping("/vote")
  private void addVote(@PathVariable String quoteId,
      @Valid @RequestBody QuoteAddVoteReqDto body) throws Exception {
    var writeModel = writeSvc.get(quoteId);
    if (writeModel == null || writeModel.getStatusDeclaration() != null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pending quote not found.");
    }

    try {
      writeModel.addVote(body.getUserId(), timeSvc.getCurrentTime());
      writeModel.save();
    } catch (IllegalStateException e) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
    }
  }

  @DeleteMapping("/vote/{userId}")
  private void removeVote(@PathVariable String quoteId, @PathVariable String userId)
      throws Exception {
    var writeModel = writeSvc.get(quoteId);
    if (writeModel == null || writeModel.getStatusDeclaration() != null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pending quote not found.");
    }

    try {
      writeModel.removeVote(userId, timeSvc.getCurrentTime());
      writeModel.save();
    } catch (IllegalStateException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vote note found.");
    }
  }

  @PostMapping("/status")
  private void declareStatus(@PathVariable String quoteId,
      @RequestBody QuoteDeclareStatusReqDto body) throws Exception {
    var writeModel = writeSvc.get(quoteId);
    if (writeModel == null || writeModel.getStatusDeclaration() != null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    try {
      writeModel.declareStatus(body.getStatus(), timeSvc.getCurrentTime());
      writeModel.save();
    } catch (IllegalStateException e) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
    }
  }
}
