package com.music.controller;

import com.music.dto.*;
import com.music.service.ArtistService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "artists")
public class ArtistController {

  @Autowired ArtistService artistService;

  @PostMapping
  public ArtistResponse createArtist(
      HttpServletResponse response, @Valid @RequestBody ArtistRequest dto) {
    response.setStatus(HttpStatus.CREATED.value());
    return artistService.createArtist(dto);
  }

  @GetMapping
  public List<ArtistResponse> getArtists() {
    return artistService.getArtists();
  }

  @GetMapping("/{id}")
  public ArtistInfoResponse getArtist(@PathVariable @NotNull UUID id) {
    return artistService.getArtist(id);
  }

  @GetMapping("/{id}/releases")
  public List<ReleaseResponse> getArtistReleases(@PathVariable @NotNull UUID id) {
    return artistService.getArtistReleases(id);
  }

  @PutMapping("/{id}")
  public ArtistResponse updateRelease(
      @PathVariable @NotNull UUID id, @Valid @RequestBody PutArtistRequest dto) {
    return artistService.updateArtist(id, dto);
  }

  @DeleteMapping("/{id}")
  public void deleteArtist(@PathVariable @NotNull UUID id) {
    artistService.deleteArtist(id);
  }
}
