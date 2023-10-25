package com.music.controller;

import com.music.dto.PostReleaseRequest;
import com.music.dto.PutReleaseRequest;
import com.music.dto.ReleaseResponse;
import com.music.service.ReleaseService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "releases")
public class ReleaseController {

  @Autowired ReleaseService releaseService;

  @PostMapping
  public ReleaseResponse createRelease(
      HttpServletResponse response, @Valid @RequestBody PostReleaseRequest dto) {
    response.setStatus(HttpStatus.CREATED.value());
    return releaseService.createRelease(dto);
  }

  @GetMapping
  public List<ReleaseResponse> getReleases() {
    return releaseService.getReleases();
  }

  @GetMapping("/{id}")
  public ReleaseResponse getRelease(@PathVariable @NotNull UUID id) {
    return releaseService.getRelease(id);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteRelease(@PathVariable @NotNull UUID id) {
    releaseService.deleteRelease(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{id}")
  public ReleaseResponse updateRelease(
      @PathVariable @NotNull UUID id, @Valid @RequestBody PutReleaseRequest dto) {
    return releaseService.updateRelease(id, dto);
  }
}
