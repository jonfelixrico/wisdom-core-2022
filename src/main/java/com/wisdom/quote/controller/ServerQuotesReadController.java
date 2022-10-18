package com.wisdom.quote.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wisdom.quote.readmodel.QuoteReadModel;
import com.wisdom.quote.readmodel.QuoteReadModelRepository;

@RestController
@RequestMapping("/server/{serverId}/quote")
public class ServerQuotesReadController {
  @Autowired
  private QuoteReadModelRepository repo;

  @GetMapping("/random")
  private QuoteReadModel getRandomQuote(@PathVariable String serverId, @RequestParam(required = false) String authorId) {
    if (authorId == null) {
      return repo.getRandomQuoteInServer(serverId);
    }
    
    return repo.getRandomQuoteInServerFilteredByAuthor(serverId, authorId);
  }
}