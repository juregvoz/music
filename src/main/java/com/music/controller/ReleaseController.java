package com.music.controller;

import com.music.dto.PostReleaseDTO;
import com.music.entity.Release;
import com.music.service.ReleaseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "releases")
public class ReleaseController {

  @Autowired ReleaseService releaseService;

  @GetMapping
  public List<Release> getReleases() {
    return releaseService.getReleases();
  }

  @PostMapping
  public Release createRelease(@Valid @RequestBody PostReleaseDTO dto) {
    return releaseService.createRelease(dto);
  }
}
