package com.wisdom.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wisdom.system.controller.dto.VersionRespDto;

@RestController
public class SystemInfoController {

  @Autowired
  private BuildProperties build;

  @GetMapping("/version")
  public VersionRespDto getVersion() {
    return new VersionRespDto(build.getVersion());
  }
}
