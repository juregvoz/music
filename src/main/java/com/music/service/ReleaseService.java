package com.music.service;

import com.music.dto.ReleasePostRequest;
import com.music.dto.ReleasePutRequest;
import com.music.dto.ReleaseResponse;

import java.util.List;
import java.util.UUID;

public interface ReleaseService {
  ReleaseResponse createRelease(ReleasePostRequest dto);

  List<ReleaseResponse> getReleases();

  ReleaseResponse getRelease(UUID id);

  ReleaseResponse updateRelease(UUID id, ReleasePutRequest dto);

  void deleteRelease(UUID id);
}
