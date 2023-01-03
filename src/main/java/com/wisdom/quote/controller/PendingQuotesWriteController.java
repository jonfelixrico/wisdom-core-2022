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
import com.wisdom.quote.writemodel.QuoteWriteModelRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

/**
 * @author Felix
 *
 */
@RestController
@RequestMapping("/pending-quote/{quoteId}")
public class PendingQuotesWriteController {

  @Autowired
  private QuoteWriteModelRepository writeSvc;

  @Autowired
  private TimeService timeSvc;

  @Operation(operationId = "addVote", summary = "Add a vote")
  @ApiResponse(responseCode = "404", description = "The pending quote was not found")
  @ApiResponse(responseCode = "403", description = "The user has already voted")
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

  @Operation(operationId = "removeVote", summary = "Remove a vote")
  @ApiResponse(responseCode = "404", description = "User has not yet voted, or the pending quote was not found")
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
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vote not found.");
    }
  }

  @Operation(operationId = "declareStatus", summary = "Declare a pending quote's status")
  @ApiResponse(responseCode = "404", description = "Pending quote was not found")
  @PostMapping("/status")
  private void declareStatus(@PathVariable String quoteId,
      @RequestBody QuoteDeclareStatusReqDto body) throws Exception {
    var writeModel = writeSvc.get(quoteId);
    if (writeModel == null ||
        writeModel.getStatusDeclaration() != null // a non-null status means that the quote is not pending anymore
    ) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    /*
     * Throws IllegalStateException, but that will only happen if we tried adding a status
     * to a non-pending quote. It should've been already handled above.
     */
    writeModel.declareStatus(body.getStatus(), timeSvc.getCurrentTime());
    writeModel.save();
  }
}
