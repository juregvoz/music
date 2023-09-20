package com.music.service;

import com.music.dto.GetReleaseDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ReleaseServiceImpl implements ReleaseService {

  public List<GetReleaseDTO> getReleases() {

    GetReleaseDTO dto = new GetReleaseDTO();
    dto.setId(UUID.randomUUID());
    dto.setName("Sandstorm");
    dto.setDescription("First release of this guy");

    return List.of(dto);
  }
}
