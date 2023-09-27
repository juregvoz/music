package com.music.service;

import com.music.dto.PostReleaseDTO;
import com.music.entity.Release;

import java.util.List;

public interface ReleaseService {
    List<Release> getReleases();

    Release createRelease(PostReleaseDTO dto);
}
