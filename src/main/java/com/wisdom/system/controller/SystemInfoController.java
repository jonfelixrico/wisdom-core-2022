package com.wisdom.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wisdom.system.controller.dto.SystemInfoDto;

@RestController
public class SystemInfoController {

  @Autowired
  private BuildProperties build;

  @GetMapping
  public SystemInfoDto getVersion() {
    return new SystemInfoDto(build.getVersion());
  }
}
