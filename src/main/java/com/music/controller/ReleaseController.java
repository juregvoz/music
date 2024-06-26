package com.music.controller;

import com.music.dto.ReleasePostRequest;
import com.music.dto.ReleasePutRequest;
import com.music.dto.ReleaseResponse;
import com.music.service.ReleaseService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "releases")
public class ReleaseController {

  private final ReleaseService releaseService;

  ReleaseController(ReleaseService releaseService) {
    this.releaseService = releaseService;
  }

  @PostMapping
  public ReleaseResponse createRelease(
      HttpServletResponse response, @Valid @RequestBody ReleasePostRequest dto) {
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

  @PutMapping("/{id}")
  public ReleaseResponse updateRelease(
      @PathVariable @NotNull UUID id, @Valid @RequestBody ReleasePutRequest dto) {
    return releaseService.updateRelease(id, dto);
  }

  @DeleteMapping("/{id}")
  public void deleteRelease(@PathVariable @NotNull UUID id) {
    releaseService.deleteRelease(id);
  }
}
