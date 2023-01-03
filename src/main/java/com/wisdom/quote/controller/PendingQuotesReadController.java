package com.wisdom.quote.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.wisdom.quote.readmodel.QuoteSnapshot;
import com.wisdom.quote.readmodel.QuoteSnapshotRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
public class PendingQuotesReadController {
  @Autowired
  private QuoteSnapshotRepository repo;

  @Operation(operationId = "getPendingQuote", summary = "Get a pending quote")
  @ApiResponses(value = {
      @ApiResponse(),
      @ApiResponse(responseCode = "404", description = "Pending quote not found", content = { @Content() })
  })
  @GetMapping("/pending-quote/{quoteId}")
  private QuoteSnapshot getPendingQuote(@PathVariable String quoteId) {
    var data = repo.findById(quoteId);
    if (data == null || data.getStatusDeclaration() != null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    return data;
  }
}
