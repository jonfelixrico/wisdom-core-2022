package com.wisdom.quote.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.webjars.NotFoundException;

import com.wisdom.quote.entity.Status;
import com.wisdom.quote.readmodel.QuoteSnapshot;
import com.wisdom.quote.readmodel.QuoteSnapshotRepository;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/server/{serverId}/quote")
public class ServerQuotesReadController {
  @Autowired
  private QuoteSnapshotRepository repo;

  @Operation(operationId = "getRandomQuote", summary = "Get a random quote from a server")
  @GetMapping("/random")
  private QuoteSnapshot getRandomQuote(@PathVariable String serverId, @RequestParam(required = false) String authorId) {
    if (authorId == null) {
      return repo.getRandomQuoteInServer(serverId);
    }

    return repo.getRandomQuoteInServerFilteredByAuthor(serverId, authorId);
  }

  @Operation(operationId = "getServerQuotes", summary = "List the quotes of a server")
  @GetMapping
  private List<QuoteSnapshot> getServerQuotes(@PathVariable String serverId,
      @RequestParam(required = false) String authorId) {
    if (authorId == null) {
      return repo.getServerQuotes(serverId);
    }

    return repo.getServerQuotes(serverId, authorId);
  }

  private boolean isQuoteApproved(QuoteSnapshot quote) {
    return quote.getStatusDeclaration() != null
        && Status.APPROVED.equals(quote.getStatusDeclaration().getStatus());
  }

  @GetMapping("/{quoteId}")
  private QuoteSnapshot getQuote(@PathVariable String serverId, @PathVariable String quoteId) {
    var quote = repo.findById(quoteId);
    if (quote == null ||
        !isQuoteApproved(quote) ||
        !quote.getServerId().equals(serverId)) {
      throw new NotFoundException("Quote not found");
    }

    return quote;
  }
}
