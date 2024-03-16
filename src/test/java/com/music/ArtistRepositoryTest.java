package com.music;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.music.entity.Artist;
import com.music.repository.ArtistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DBRider
public class ArtistRepositoryTest {

  @Autowired ArtistRepository artistRepository;

  @BeforeEach
  @DataSet(cleanBefore = true)
  void cleanDatabase() {}

  @Test
  @ExpectedDataSet(value = "artist.yaml", ignoreCols = "id")
  void saveArtist_success() {
    artistRepository.save(TestData.artist());
  }

  @Test
  @DataSet(value = "artist.yaml")
  void getArtist_success() {
    Optional<Artist> optional =
        artistRepository.findById(UUID.fromString("f0e71426-ae75-4a67-985f-d564a0458bad"));

    Artist artist = optional.get();
    Artist testArtist = TestData.artist();

    assertAll(
        () -> assertEquals(testArtist.getId(), artist.getId()),
        () -> assertEquals(testArtist.getName(), artist.getName()),
        () -> assertEquals(testArtist.getDescription(), artist.getDescription()));
  }

  @Test
  @DataSet(value = "fullDatabase.yaml")
  void getArtists_success() {
    List<Artist> artists = artistRepository.findAll();
    assertEquals(2, artists.size());
  }

  @Test
  @DataSet(value = "artist.yaml")
  @ExpectedDataSet(value = "artistUpdated.yaml")
  void updateArtist_success() {
    artistRepository.save(TestData.artistUpdated());
  }

  @Test
  @DataSet(value = "artist.yaml")
  @ExpectedDataSet(value = "emptyDatabase.yaml")
  void deleteArtist_success() {
    artistRepository.delete(TestData.artist());
  }
}
