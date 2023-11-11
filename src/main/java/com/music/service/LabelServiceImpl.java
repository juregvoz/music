package com.music.service;

import com.music.dto.*;
import com.music.entity.Label;
import com.music.repository.LabelRepository;
import com.music.utils.Utils;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LabelServiceImpl implements LabelService {

  @Autowired private LabelRepository labelRepository;

  @Autowired private ModelMapper modelMapper;

  @Autowired private Utils utils;

  @Override
  @Transactional
  public LabelResponse createLabel(LabelPostRequest dto) {
    Label label = labelRepository.save(modelMapper.map(dto, Label.class));
    return modelMapper.map(label, LabelResponse.class);
  }

  @Override
  @Transactional
  public List<LabelResponse> getLabels() {
    return labelRepository.findAll().stream()
        .map(label -> modelMapper.map(label, LabelResponse.class))
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public LabelInfoResponse getLabel(UUID id) {
    return modelMapper.map(findById(id), LabelInfoResponse.class);
  }

  @Override
  @Transactional
  public Label getLabelEntity(UUID id) {
    return findById(id);
  }

  @Override
  @Transactional
  public List<ReleaseResponse> getLabelReleases(UUID id) {
    return findById(id).getReleases().stream()
        .map(release -> modelMapper.map(release, ReleaseResponse.class))
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public LabelResponse updateLabel(UUID id, LabelPutRequest dto) {
    final Label label = findById(id);
    utils.updateIfPresent(dto.getName(), label::setName);
    utils.updateIfPresent(dto.getDescription(), label::setDescription);
    return modelMapper.map(labelRepository.save(label), LabelResponse.class);
  }

  @Override
  @Transactional
  public void deleteLabel(UUID id) {
    if (!getLabelReleases(id).isEmpty()) {
      throw Problem.builder()
          .withStatus(Status.UNPROCESSABLE_ENTITY)
          .withDetail(
              String.format("Label with id [%s] has related releases and can't be deleted", id))
          .build();
    }
    labelRepository.delete(findById(id));
  }

  private Label findById(UUID id) {
    return labelRepository
        .findById(id)
        .orElseThrow(
            () ->
                Problem.builder()
                    .withStatus(Status.NOT_FOUND)
                    .withDetail(String.format("Label with id [%s] not found", id))
                    .build());
  }
}
