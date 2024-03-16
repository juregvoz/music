package com.music;

import com.music.dto.*;
import com.music.entity.Label;
import com.music.repository.LabelRepository;
import com.music.service.LabelServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.zalando.problem.DefaultProblem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LabelServiceTest {

  LabelRepository labelRepository;
  ModelMapper modelMapper;
  LabelServiceImpl labelService;

  @BeforeAll
  private void setup() {
    this.labelRepository = Mockito.mock(LabelRepository.class);
    this.modelMapper = new ModelMapper();
    this.labelService = new LabelServiceImpl(labelRepository, modelMapper);
  }

  @Test
  void createLabel_success() {
    // arrange
    LabelPostRequest dto = TestData.labelPostRequest();
    Label label = TestData.label();
    doReturn(label).when(labelRepository).save(any());

    // act
    LabelResponse response = labelService.createLabel(dto);

    // assert
    assertAll(
        () -> assertEquals(label.getId(), response.getId()),
        () -> assertEquals(dto.getName(), response.getName()),
        () -> assertEquals(dto.getDescription(), response.getDescription()));
  }

  @Test
  void getLabels_success() {
    // arrange
    doReturn(List.of(TestData.label(), TestData.labelWithRelease()))
        .when(labelRepository)
        .findAll();

    // act
    List<LabelResponse> labels = labelService.getLabels();

    // assert
    assertAll(
        () -> assertEquals(2, (long) labels.size()),
        () -> assertEquals(TestData.labelResponse(), labels.get(0)));
  }

  @Test
  void getLabel_success() {
    // arrange
    Label label = TestData.labelWithRelease();
    UUID id = label.getId();
    doReturn(Optional.of(label)).when(labelRepository).findById(id);

    // act
    LabelInfoResponse response = labelService.getLabel(id);

    // assert
    assertAll(
        () -> assertEquals(id, response.getId()),
        () -> assertEquals(label.getName(), response.getName()),
        () -> assertEquals(label.getDescription(), response.getDescription()),
        () -> assertEquals(List.of(TestData.releaseResponse()), response.getReleases()));
  }

  @Test
  void getLabel_notFound_throwsException() {
    UUID id = UUID.randomUUID();
    doReturn(Optional.empty()).when(labelRepository).findById(id);

    // act
    Executable executable = () -> labelService.getLabel(id);

    // assert
    DefaultProblem problem = assertThrows(DefaultProblem.class, executable);
    assertEquals(String.format("Label with id [%s] not found", id), problem.getMessage());
  }

  @Test
  void updateLabel_success() {
    // arrange
    UUID id = TestData.label().getId();
    doReturn(Optional.of(TestData.label())).when(labelRepository).findById(id);
    doReturn(TestData.labelUpdated()).when(labelRepository).save(any());
    LabelPutRequest dto = TestData.labelPutRequest();

    // act
    LabelResponse updatedLabel = labelService.updateLabel(id, dto);

    // assert
    assertAll(() -> assertEquals(TestData.labelUpdatedResponse(), updatedLabel));
  }

  @Test
  void deleteLabel_success() {
    // arrange
    doReturn(Optional.of(TestData.label())).when(labelRepository).findById(any());

    // act
    labelService.deleteLabel(UUID.randomUUID());

    // assert
    verify(labelRepository, times(1)).delete(any());
  }

  @Test
  void deleteLabel_hasRelatedReleases_throwsException() {
    // arrange
    UUID id = UUID.randomUUID();
    doReturn(Optional.of(TestData.labelWithRelease())).when(labelRepository).findById(id);

    // act
    Executable executable = () -> labelService.deleteLabel(id);

    // assert
    DefaultProblem problem = assertThrows(DefaultProblem.class, executable);
    assertEquals(
        String.format("Label with id [%s] has related releases and can't be deleted", id),
        problem.getMessage());
  }
}
