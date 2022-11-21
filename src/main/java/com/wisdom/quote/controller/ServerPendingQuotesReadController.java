package com.wisdom.quote.controller;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wisdom.quote.readmodel.QuoteSnapshot;
import com.wisdom.quote.readmodel.QuoteSnapshotRepo;

@RestController
public class ServerPendingQuotesReadController {
  @Autowired
  private QuoteSnapshotRepo repo;

  @GetMapping("/server/{serverId}/pending-quote")
  private List<QuoteSnapshot> getServerPendingQuotes(@PathVariable String serverId,
      @RequestParam Optional<Instant> expiringBefore) {
    if (expiringBefore.isPresent()) {
      return repo.getExpiringPendingeQuotes(serverId, expiringBefore.get());
    }

    return repo.findPendingQuotesInServer(serverId);
  }
}
