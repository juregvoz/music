package com.music.controller;

import com.music.dto.GetReleaseDTO;
import com.music.service.ReleaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "releases")
public class ReleaseController {

  @Autowired ReleaseService releaseService;

  @GetMapping
  public List<GetReleaseDTO> getReleases() {
    return releaseService.getReleases();
  }
}
