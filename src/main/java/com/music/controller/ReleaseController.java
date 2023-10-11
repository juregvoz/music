package com.music.controller;

import com.music.dto.PostReleaseDTO;
import com.music.dto.PutReleaseDTO;
import com.music.entity.Release;
import com.music.service.ReleaseService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "releases")
public class ReleaseController {

  @Autowired ReleaseService releaseService;

  @GetMapping
  public List<Release> getReleases() {
    return releaseService.getReleases();
  }

  @GetMapping("/{id}")
  public Release getRelease(@PathVariable @NotNull UUID id) {
    return releaseService.getRelease(id);
  }

  @PostMapping
  public Release createRelease(@Valid @RequestBody PostReleaseDTO dto) {
    return releaseService.createRelease(dto);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteRelease(@PathVariable @NotNull UUID id) {
    releaseService.deleteRelease(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{id}")
  public Release updateRelease(
      @PathVariable @NotNull UUID id, @Valid @RequestBody PutReleaseDTO dto) {
    return releaseService.updateRelease(id, dto);
  }
}
