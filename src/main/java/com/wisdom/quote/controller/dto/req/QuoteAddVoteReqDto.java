package com.wisdom.quote.controller.dto.req;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class QuoteAddVoteReqDto {
  @NotNull
  @NotBlank
  private String userId;

  public String getUserId() {
    return userId;
  }

}
