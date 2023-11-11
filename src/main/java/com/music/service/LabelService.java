package com.music.service;

import com.music.dto.*;
import com.music.entity.Label;

import java.util.List;
import java.util.UUID;

public interface LabelService {
  LabelResponse createLabel(LabelPostRequest dto);

  List<LabelResponse> getLabels();

  LabelInfoResponse getLabel(UUID id);

  Label getLabelEntity(UUID id);

  List<ReleaseResponse> getLabelReleases(UUID id);

  LabelResponse updateLabel(UUID id, LabelPutRequest dto);

  void deleteLabel(UUID id);
}
