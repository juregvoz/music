package com.music.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.music.dto.PostReleaseDTO;
import com.music.dto.PutReleaseDTO;
import com.music.entity.Release;
import com.music.repository.ReleaseRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zalando.problem.*;

import java.util.List;
import java.util.UUID;

@Service
public class ReleaseServiceImpl implements ReleaseService {

  @Autowired private ReleaseRepository releaseRepository;

  @Autowired private ObjectMapper objectMapper;

  @Override
  @Transactional
  public Release createRelease(PostReleaseDTO dto) {
    return releaseRepository.save(objectMapper.convertValue(dto, Release.class));
  }

  @Override
  @Transactional
  public List<Release> getReleases() {
    return releaseRepository.findAll();
  }

  @Override
  @Transactional
  public Release getRelease(UUID id) {
    return findById(id);
  }

  @Override
  @Transactional
  public Release updateRelease(UUID id, PutReleaseDTO dto) {
    final Release release = findById(id);
    if (dto.getDescription() != null) {
      release.setDescription(dto.getDescription());
    }
    if (dto.getName() != null) {
      release.setName(dto.getName());
    }
    return releaseRepository.save(release);
  }

  @Override
  @Transactional
  public void deleteRelease(UUID id) {
    final Release release = findById(id);
    releaseRepository.delete(release);
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
