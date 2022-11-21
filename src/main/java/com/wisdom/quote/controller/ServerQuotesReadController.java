package com.wisdom.quote.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wisdom.quote.readmodel.QuoteSnapshot;
import com.wisdom.quote.readmodel.QuoteSnapshotRepo;

@RestController
@RequestMapping("/server/{serverId}/quote")
public class ServerQuotesReadController {
  @Autowired
  private QuoteSnapshotRepo repo;

  @GetMapping("/random")
  private QuoteSnapshot getRandomQuote(@PathVariable String serverId, @RequestParam(required = false) String authorId) {
    if (authorId == null) {
      return repo.getRandomQuoteInServer(serverId);
    }

    return repo.getRandomQuoteInServerFilteredByAuthor(serverId, authorId);
  }

  @GetMapping
  private List<QuoteSnapshot> getServerQuotes(@PathVariable String serverId,
      @RequestParam(required = false) String authorId) {
    if (authorId == null) {
      return repo.getServerQuotes(serverId);
    }

    return repo.getServerQuotes(serverId, authorId);
  }
}
