package com.wisdom.system.controller.dto;

public class SystemInfoDto {
  private String build;

  public SystemInfoDto(String build) {
    this.build = build;
  }

  public String getBuild() {
    return build;
  }

}
