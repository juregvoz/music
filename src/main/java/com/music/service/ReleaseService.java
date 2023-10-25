package com.music.service;

import com.music.dto.PostReleaseRequest;
import com.music.dto.PutReleaseRequest;
import com.music.dto.ReleaseResponse;

import java.util.List;
import java.util.UUID;

public interface ReleaseService {
  ReleaseResponse createRelease(PostReleaseRequest dto);

  List<ReleaseResponse> getReleases();

  ReleaseResponse getRelease(UUID id);

  ReleaseResponse updateRelease(UUID id, PutReleaseRequest dto);

  void deleteRelease(UUID id);
}
