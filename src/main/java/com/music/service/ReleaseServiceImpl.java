package com.music.service;

import com.music.dto.PostReleaseRequest;
import com.music.dto.PutReleaseRequest;
import com.music.dto.ReleaseResponse;
import com.music.entity.Release;
import com.music.repository.ReleaseRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zalando.problem.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReleaseServiceImpl implements ReleaseService {

  @Autowired private ReleaseRepository releaseRepository;

  @Autowired private ModelMapper modelMapper;

  @Autowired private ArtistService artistService;

  @Override
  @Transactional
  public ReleaseResponse createRelease(PostReleaseRequest dto) {
    Release release = modelMapper.map(dto, Release.class);
    release.setArtist(artistService.getArtistEntity(dto.getArtistId()));
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
  public ReleaseResponse updateRelease(UUID id, PutReleaseRequest dto) {
    final Release release = findById(id);
    if (dto.getDescription() != null) {
      release.setDescription(dto.getDescription());
    }
    if (dto.getName() != null) {
      release.setName(dto.getName());
    }
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
