package com.wisdom.quote.controller.v2;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "server-quotes")
@RestController
@RequestMapping("/v2/server/{serverId}/quote")
public class ServerQuotesReadControllerV2 {
  @Autowired
  private QuoteSnapshotRepository repo;

  @Operation(operationId = "listQuotesV2", summary = "List the quotes of a server")
  @GetMapping
  private List<QuoteSnapshot> listQuotes(@PathVariable String serverId,
      @RequestParam(defaultValue = "20") Integer limit,
      @RequestParam(required = false) String after) {
    return repo.listServerQuotes(serverId, limit, after);
  }

  private boolean isQuoteExpired(QuoteSnapshot quote) {
    return quote.getStatusDeclaration() != null && Status.EXPIRED.equals(quote.getStatusDeclaration().getStatus());
  }

  @Operation(operationId = "getQuoteV2", summary = "Get the quote of a server")
  @ApiResponses(value = {
      @ApiResponse(),
      @ApiResponse(responseCode = "404", content = {
          @Content() })
  })
  @GetMapping("/{quoteId}")
  private QuoteSnapshot getQuote(@PathVariable String serverId, @PathVariable String quoteId) {
    var quote = repo.findById(quoteId);
    if (quote == null ||
        isQuoteExpired(quote) ||
        !quote.getServerId().equals(serverId)) {
      throw new NotFoundException("Quote not found");
    }

    return quote;
  }
}
