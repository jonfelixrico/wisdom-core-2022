package com.wisdom.quote.controller.v2;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.wisdom.quote.readmodel.QuoteSnapshot;
import com.wisdom.quote.readmodel.QuoteSnapshotRepository;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/v2/server/{serverId}/quote")
public class ServerQuotesReadControllerV2 {
  @Autowired
  private QuoteSnapshotRepository repo;

  @Operation(operationId = "getServerQuotesV2", summary = "List the quotes of a server (includes approved and pending quotes)")
  @GetMapping
  private List<QuoteSnapshot> getServerQuotes(@PathVariable String serverId,
      @RequestParam(required = false) String contentId, @RequestParam(required = false) String limit,
      @RequestParam(required = false) String after) {
    throw new RuntimeException("noop");
  }
}
