package com.music.service;

import com.music.dto.PostReleaseDTO;
import com.music.dto.PutReleaseDTO;
import com.music.entity.Release;

import java.util.List;
import java.util.UUID;

public interface ReleaseService {
  Release createRelease(PostReleaseDTO dto);

  List<Release> getReleases();

  Release getRelease(UUID id);

  Release updateRelease(UUID id, PutReleaseDTO dto);

  void deleteRelease(UUID id);
}
