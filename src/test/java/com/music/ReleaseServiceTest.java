package com.music;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.music.configuration.GeneralConfiguration;
import com.music.dto.ReleasePostRequest;
import com.music.dto.ReleasePutRequest;
import com.music.dto.ReleaseResponse;
import com.music.entity.Release;
import com.music.repository.ReleaseRepository;
import com.music.service.*;
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

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReleaseServiceTest {
  ReleaseRepository releaseRepository;
  ArtistService artistService;
  LabelService labelService;
  ModelMapper modelMapper;

  ReleaseServiceImpl releaseService;

  @BeforeAll
  private void setup() {
    this.releaseRepository = Mockito.mock(ReleaseRepository.class);
    this.artistService = Mockito.mock(ArtistService.class);
    this.labelService = Mockito.mock(LabelService.class);
    this.modelMapper = GeneralConfiguration.modelMapper();
    this.releaseService =
        new ReleaseServiceImpl(releaseRepository, artistService, labelService, modelMapper);
  }

  @Test
  void createRelease_success() {
    // arrange
    ReleasePostRequest dto = TestData.releasePostRequest();
    Release release = TestData.release();
    doReturn(release).when(releaseRepository).save(any());

    // act
    ReleaseResponse response = releaseService.createRelease(dto);

    // assert
    assertAll(
        () -> assertEquals(release.getId(), response.getId()),
        () -> assertEquals(dto.getName(), response.getName()),
        () -> assertEquals(dto.getDescription(), response.getDescription()));
    verify(artistService, times(1)).getArtistEntity(dto.getArtistId());
    verify(labelService, times(1)).getLabelEntity(dto.getLabelId());
  }

  @Test
  void getReleases_success() {
    // arrange
    doReturn(List.of(TestData.release(), TestData.release())).when(releaseRepository).findAll();

    // act
    List<ReleaseResponse> releases = releaseService.getReleases();

    // assert
    assertAll(
        () -> assertEquals(2, (long) releases.size()),
        () -> assertEquals(TestData.releaseResponse(), releases.get(0)));
  }

  @Test
  void getRelease_success() {
    // arrange
    Release release = TestData.release();
    UUID id = release.getId();
    doReturn(Optional.of(release)).when(releaseRepository).findById(id);

    // act
    ReleaseResponse response = releaseService.getRelease(id);

    // assert
    assertEquals(TestData.releaseResponse(), response);
  }

  @Test
  void getRelease_notFound_throwsException() {
    UUID id = UUID.fromString("e721c1f4-5335-47a2-90d0-734d3328b0f2");
    doReturn(Optional.empty()).when(releaseRepository).findById(id);

    // act
    Executable executable = () -> releaseService.getRelease(id);

    // assert
    DefaultProblem problem = assertThrows(DefaultProblem.class, executable);
    assertEquals(
        "Release with id [e721c1f4-5335-47a2-90d0-734d3328b0f2] not found", problem.getMessage());
  }

  @Test
  void updateRelease_success() {
    // arrange
    doReturn(Optional.of(TestData.release())).when(releaseRepository).findById(any());
    doReturn(TestData.releaseUpdated()).when(releaseRepository).save(any());
    ReleasePutRequest dto = TestData.releasePutRequest();

    // act
    ReleaseResponse updatedRelease = releaseService.updateRelease(TestData.release().getId(), dto);

    // assert
    assertAll(() -> assertEquals(TestData.releaseUpdatedResponse(), updatedRelease));
  }

  @Test
  void deleteRelease_success() {
    // arrange
    doReturn(Optional.of(TestData.release())).when(releaseRepository).findById(any());

    // act
    releaseService.deleteRelease(UUID.randomUUID());

    // assert
    verify(releaseRepository, times(1)).delete(any());
  }
}
