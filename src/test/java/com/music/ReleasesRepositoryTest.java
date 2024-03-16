package com.music;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.music.entity.Release;
import com.music.repository.ReleaseRepository;
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
public class ReleasesRepositoryTest {

  @Autowired ReleaseRepository releaseRepository;

  @BeforeEach
  @DataSet(cleanBefore = true)
  void cleanDatabase() {}

  @Test
  @DataSet("artistLabel.yaml")
  @ExpectedDataSet(value = "artistLabelRelease.yaml", ignoreCols = "id")
  void saveRelease_success() {
    releaseRepository.save(TestData.release());
  }

  @Test
  @DataSet(value = "artistLabelRelease.yaml")
  void getRelease_success() {
    Optional<Release> optional =
        releaseRepository.findById(UUID.fromString("3e7c8e4f-8666-48a9-9e69-ffb8e74585c9"));

    Release release = optional.get();
    Release testRelease = TestData.release();

    assertAll(
        () -> assertEquals(testRelease.getId(), release.getId()),
        () -> assertEquals(testRelease.getName(), release.getName()),
        () -> assertEquals(testRelease.getDescription(), release.getDescription()),
        () -> assertEquals(testRelease.getArtist().getId(), release.getArtist().getId()),
        () -> assertEquals(testRelease.getLabel().getId(), release.getLabel().getId()));
  }

  @Test
  @DataSet(value = "fullDatabase.yaml")
  void getReleases_success() {
    List<Release> releases = releaseRepository.findAll();
    assertEquals(2, releases.size());
  }

  @Test
  @DataSet("artistLabelRelease.yaml")
  @ExpectedDataSet("artistLabelReleaseUpdated.yaml")
  void updateRelease_success() {
    releaseRepository.save(TestData.releaseUpdated());
  }

  @Test
  @DataSet("artistLabelRelease.yaml")
  @ExpectedDataSet("artistLabel.yaml")
  void deleteRelease_success() {
    releaseRepository.delete(TestData.release());
  }
}
