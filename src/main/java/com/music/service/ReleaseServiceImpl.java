package com.music.service;

import com.music.dto.*;
import com.music.entity.Release;
import com.music.repository.ReleaseRepository;
import com.music.utils.Utils;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.zalando.problem.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReleaseServiceImpl implements ReleaseService {

  private final ReleaseRepository releaseRepository;
  private final ModelMapper modelMapper;
  private final ArtistService artistService;
  private final LabelService labelService;

  ReleaseServiceImpl(
      ReleaseRepository releaseRepository,
      ArtistService artistService,
      LabelService labelService,
      ModelMapper modelMapper) {
    this.releaseRepository = releaseRepository;
    this.artistService = artistService;
    this.labelService = labelService;
    this.modelMapper = modelMapper;
  }

  @Override
  @Transactional
  public ReleaseResponse createRelease(ReleasePostRequest dto) {
    Release release = modelMapper.map(dto, Release.class);
    release.setArtist(artistService.getArtistEntity(dto.getArtistId()));
    release.setLabel(labelService.getLabelEntity(dto.getLabelId()));
    return modelMapper.map(releaseRepository.save(release), ReleaseResponse.class);
  }

  @Override
  @Transactional
  public List<ReleaseResponse> getReleases() {
    return releaseRepository.findAll().stream()
        .map(release -> modelMapper.map(release, ReleaseResponse.class))
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public ReleaseResponse getRelease(UUID id) {
    return modelMapper.map(findById(id), ReleaseResponse.class);
  }

  @Override
  @Transactional
  public ReleaseResponse updateRelease(UUID releaseId, ReleasePutRequest dto) {
    final Release release = findById(releaseId);
    Utils.updateIfPresent(dto.getName(), release::setName);
    Utils.updateIfPresent(dto.getDescription(), release::setDescription);
    Utils.updateIfPresent(
        dto.getArtistId(), id -> release.setArtist(artistService.getArtistEntity(id)));
    Utils.updateIfPresent(
        dto.getLabelId(), id -> release.setLabel(labelService.getLabelEntity(id)));
    return modelMapper.map(releaseRepository.save(release), ReleaseResponse.class);
  }

  @Override
  @Transactional
  public void deleteRelease(UUID id) {
    releaseRepository.delete(findById(id));
  }

  private Release findById(UUID id) {
    return releaseRepository
        .findById(id)
        .orElseThrow(
            () ->
                Problem.builder()
                    .withStatus(Status.NOT_FOUND)
                    .withDetail(String.format("Release with id [%s] not found", id))
                    .build());
  }
}
