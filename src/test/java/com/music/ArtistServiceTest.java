package com.music;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.music.dto.ArtistInfoResponse;
import com.music.dto.ArtistPostRequest;
import com.music.dto.ArtistPutRequest;
import com.music.dto.ArtistResponse;
import com.music.entity.Artist;
import com.music.repository.ArtistRepository;
import com.music.service.ArtistServiceImpl;
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
public class ArtistServiceTest {
  ArtistRepository artistRepository;
  ModelMapper modelMapper;
  ArtistServiceImpl artistService;

  @BeforeAll
  private void setup() {
    this.artistRepository = Mockito.mock(ArtistRepository.class);
    this.modelMapper = new ModelMapper();
    this.artistService = new ArtistServiceImpl(artistRepository, modelMapper);
  }

  @Test
  void createArtist_success() {
    // arrange
    ArtistPostRequest dto = TestData.artistPostRequest();
    Artist artist = TestData.artist();
    doReturn(artist).when(artistRepository).save(any());

    // act
    ArtistResponse response = artistService.createArtist(dto);

    // assert
    assertAll(
        () -> assertEquals(artist.getId(), response.getId()),
        () -> assertEquals(dto.getName(), response.getName()),
        () -> assertEquals(dto.getDescription(), response.getDescription()));
  }

  @Test
  void getArtists_success() {
    // arrange
    doReturn(List.of(TestData.artist(), TestData.artistWithRelease()))
        .when(artistRepository)
        .findAll();

    // act
    List<ArtistResponse> artists = artistService.getArtists();

    // assert
    assertAll(
        () -> assertEquals(2, (long) artists.size()),
        () -> assertEquals(TestData.artistResponse(), artists.get(0)));
  }

  @Test
  void getArtist_success() {
    // arrange
    Artist artist = TestData.artistWithRelease();
    UUID id = artist.getId();
    doReturn(Optional.of(artist)).when(artistRepository).findById(id);

    // act
    ArtistInfoResponse response = artistService.getArtist(id);

    // assert
    assertAll(
        () -> assertEquals(id, response.getId()),
        () -> assertEquals(artist.getName(), response.getName()),
        () -> assertEquals(artist.getDescription(), response.getDescription()),
        () -> assertEquals(List.of(TestData.releaseResponse()), response.getReleases()));
  }

  @Test
  void getArtist_notFound_throwsException() {
    UUID id = UUID.fromString("e721c1f4-5335-47a2-90d0-734d3328b0f2");
    doReturn(Optional.empty()).when(artistRepository).findById(id);

    // act
    Executable executable = () -> artistService.getArtist(id);

    // assert
    DefaultProblem problem = assertThrows(DefaultProblem.class, executable);
    assertEquals(
        "Artist with id [e721c1f4-5335-47a2-90d0-734d3328b0f2] not found", problem.getMessage());
  }

  @Test
  void updateArtist_success() {
    // arrange
    doReturn(Optional.of(TestData.artist())).when(artistRepository).findById(any());
    doReturn(TestData.artistUpdated()).when(artistRepository).save(any());
    ArtistPutRequest dto = TestData.artistPutRequest();

    // act
    ArtistResponse updatedArtist = artistService.updateArtist(TestData.artist().getId(), dto);

    // assert
    assertAll(() -> assertEquals(TestData.artistUpdatedResponse(), updatedArtist));
  }

  @Test
  void deleteArtist_success() {
    // arrange
    doReturn(Optional.of(TestData.artist())).when(artistRepository).findById(any());

    // act
    artistService.deleteArtist(UUID.randomUUID());

    // assert
    verify(artistRepository, times(1)).delete(any());
  }

  @Test
  void deleteArtist_hasRelatedReleases_throwsException() {
    // arrange
    UUID id = UUID.randomUUID();
    doReturn(Optional.of(TestData.artistWithRelease())).when(artistRepository).findById(id);

    // act
    Executable executable = () -> artistService.deleteArtist(id);

    // assert
    DefaultProblem problem = assertThrows(DefaultProblem.class, executable);
    assertEquals(
        String.format("Artist with id [%s] has related releases and can't be deleted", id),
        problem.getMessage());
  }
}
