package com.music.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.music.dto.PostReleaseDTO;
import com.music.entity.Release;
import com.music.repository.ReleaseRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReleaseServiceImpl implements ReleaseService {

  @Autowired private ReleaseRepository releaseRepository;

  @Autowired private ObjectMapper objectMapper;

  @Override
  @Transactional
  public List<Release> getReleases() {
    return releaseRepository.findAll();
  }

  @Override
  @Transactional
  public Release createRelease(PostReleaseDTO dto) {
    return releaseRepository.save(objectMapper.convertValue(dto, Release.class));
  }
}
