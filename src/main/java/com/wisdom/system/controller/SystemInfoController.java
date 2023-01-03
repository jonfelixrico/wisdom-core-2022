package com.wisdom.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wisdom.system.controller.dto.VersionRespDto;

import io.swagger.v3.oas.annotations.Operation;

@RestController
public class SystemInfoController {

  @Autowired
  private BuildProperties build;

  @Operation(operationId = "getAppVersion", summary = "Get the Wisdom Core version")
  @GetMapping("/version")
  public VersionRespDto getVersion() {
    return new VersionRespDto(build.getVersion());
  }
}
