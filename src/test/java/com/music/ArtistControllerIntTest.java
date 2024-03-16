package com.music;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.music.dto.ArtistInfoResponse;
import com.music.dto.ArtistPostRequest;
import com.music.dto.ArtistPutRequest;
import com.music.dto.ArtistResponse;
import java.util.List;
import java.util.UUID;

import com.music.dto.Error;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.zalando.problem.DefaultProblem;

@SpringBootTest
@ActiveProfiles("test")
@DBRider
@AutoConfigureMockMvc
public class ArtistControllerIntTest {

  @Autowired MockMvc mockMvc;

  @Autowired ObjectMapper objectMapper;

  @BeforeEach
  @DataSet(cleanBefore = true)
  void cleanDatabase() {}

  @Test
  @ExpectedDataSet(value = "artist.yaml", ignoreCols = "id")
  void createArist_success() throws Exception {
    ArtistPostRequest artistPostRequest = TestData.artistPostRequest();
    String contentString =
        mockMvc
            .perform(
                post("/artists")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(artistPostRequest)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    ArtistResponse content = objectMapper.readValue(contentString, ArtistResponse.class);

    // assert
    assertAll(
        () -> Assertions.assertEquals(artistPostRequest.getDescription(), content.getDescription()),
        () -> Assertions.assertEquals(artistPostRequest.getName(), content.getName()),
        () -> assertInstanceOf(UUID.class, content.getId()));
  }

  @Test
  void createArtist_noName_throwsException() throws Exception {
    ArtistPostRequest artistPostRequest = new ArtistPostRequest();
    String contentString =
        mockMvc
            .perform(
                post("/artists")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(artistPostRequest)))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

    Error error = objectMapper.readValue(contentString, Error.class);

    // assert
    assertEquals("[{name=must not be null}]", error.getMessage());
  }

  @Test
  @DataSet("fullDatabase.yaml")
  void getArtists_success() throws Exception {
    String contentString =
        mockMvc
            .perform(get("/artists"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    List<ArtistResponse> content = objectMapper.readValue(contentString, new TypeReference<>() {});

    // assert
    assertEquals(2, content.toArray().length);
  }

  @Test
  @DataSet("fullDatabase.yaml")
  void getArtist_success() throws Exception {
    ArtistInfoResponse artist = TestData.artistInfoResponse();
    String contentString =
        mockMvc
            .perform(get(String.format("/artists/%s", artist.getId())))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    ArtistInfoResponse content = objectMapper.readValue(contentString, ArtistInfoResponse.class);

    // assert
    assertEquals(artist, content);
  }

  @Test
  @DataSet("emptyDatabase.yaml")
  void getArtist_notFound_throwsException() throws Exception {
    UUID id = UUID.fromString("74377b54-67f5-4469-b7fe-d225d7d3902e");

    Exception exception =
        mockMvc
            .perform(get(String.format("/artists/%s", id)))
            .andExpect(status().isNotFound())
            .andReturn()
            .getResolvedException();

    // assert
    assertAll(
        () -> assertInstanceOf(DefaultProblem.class, exception),
        () ->
            assertEquals(
                String.format("Artist with id [%s] not found", id), exception.getMessage()));
  }

  @Test
  @DataSet(value = "artist.yaml")
  @ExpectedDataSet(value = "artistUpdated.yaml")
  void updateArtist_success() throws Exception {
    UUID id = TestData.artistResponse().getId();
    ArtistPutRequest artistPutRequest = TestData.artistPutRequest();
    String contentString =
        mockMvc
            .perform(
                put(String.format("/artists/%s", id))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(artistPutRequest)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    ArtistResponse artistResponse = objectMapper.readValue(contentString, ArtistResponse.class);

    assertEquals(TestData.artistUpdatedResponse(), artistResponse);
  }

  @Test
  @DataSet("artist.yaml")
  @ExpectedDataSet("emptyDatabase.yaml")
  void deleteArtist_success() throws Exception {
    UUID id = TestData.artistResponse().getId();
    mockMvc.perform(delete(String.format("/artists/%s", id))).andExpect(status().isOk());
  }

  @Test
  @DataSet("fullDatabase.yaml")
  void deleteArtist_hasRelatedReleases_throwsException() throws Exception {
    UUID id = TestData.artistResponse().getId();
    Exception exception =
        mockMvc
            .perform(delete(String.format("/artists/%s", id)))
            .andExpect(status().isUnprocessableEntity())
            .andReturn()
            .getResolvedException();

    assertAll(
        () -> assertInstanceOf(DefaultProblem.class, exception),
        () ->
            assertEquals(
                String.format("Artist with id [%s] has related releases and can't be deleted", id),
                exception.getMessage()));
  }
}
