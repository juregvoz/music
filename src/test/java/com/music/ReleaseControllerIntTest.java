package com.music;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.music.dto.*;
import com.music.dto.Error;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.zalando.problem.DefaultProblem;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@DBRider
@AutoConfigureMockMvc
@Disabled
public class ReleaseControllerIntTest {

  @Autowired MockMvc mockMvc;

  @Autowired ObjectMapper objectMapper;

  @BeforeEach
  @DataSet(cleanBefore = true)
  void cleanDatabase() {}

  @Test
  @DataSet("artistLabel.yaml")
  @ExpectedDataSet(value = "artistLabelRelease.yaml", ignoreCols = "id")
  void createRelease_success() throws Exception {
    ReleasePostRequest releasePostRequest = TestData.releasePostRequest();
    String contentString =
        mockMvc
            .perform(
                post("/releases")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(releasePostRequest)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    ReleaseResponse content = objectMapper.readValue(contentString, ReleaseResponse.class);

    // assert
    assertAll(
        () ->
            Assertions.assertEquals(releasePostRequest.getDescription(), content.getDescription()),
        () -> Assertions.assertEquals(releasePostRequest.getName(), content.getName()),
        () -> Assertions.assertEquals(releasePostRequest.getArtistId(), content.getArtistId()),
        () -> Assertions.assertEquals(releasePostRequest.getLabelId(), content.getLabelId()),
        () -> assertInstanceOf(UUID.class, content.getId()));
  }

  @Test
  void createRelease_noRequiredFields_throwsException() throws Exception {
    ReleasePostRequest releasePostRequest = new ReleasePostRequest();
    String contentString =
        mockMvc
            .perform(
                post("/releases")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(releasePostRequest)))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

    Error error = objectMapper.readValue(contentString, Error.class);

    // assert
    assertEquals(
        "[{artistId=must not be null}, {labelId=must not be null}, {name=must not be null}]",
        error.getMessage());
  }

  @DataSet("artistLabel.yaml")
  @ParameterizedTest
  @CsvSource({
    "f0e71426-ae75-4a67-985f-d564a0458bad,Label",
    "82d41545-50c3-44b4-be2c-3585080985be,Artist",
  })
  void createRelease_somethingNotFound_throwsException(UUID id, String field) throws Exception {
    ReleasePostRequest releasePostRequest = new ReleasePostRequest();
    releasePostRequest.setName("Never gonna give you up");
    releasePostRequest.setArtistId(id);
    releasePostRequest.setLabelId(id);

    Exception exception =
        mockMvc
            .perform(
                post("/releases")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(releasePostRequest)))
            .andExpect(status().isNotFound())
            .andReturn()
            .getResolvedException();

    // assert
    assertAll(
        () -> assertInstanceOf(DefaultProblem.class, exception),
        () ->
            assertEquals(
                String.format("%s with id [%s] not found", field, id), exception.getMessage()));
  }

  @Test
  @DataSet("fullDatabase.yaml")
  void getReleases_success() throws Exception {
    String contentString =
        mockMvc
            .perform(get("/releases"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    List<ReleaseResponse> content = objectMapper.readValue(contentString, new TypeReference<>() {});

    // assert
    assertEquals(2, content.toArray().length);
  }

  @Test
  @DataSet("fullDatabase.yaml")
  void getRelease_success() throws Exception {
    ReleaseResponse release = TestData.releaseResponse();
    String contentString =
        mockMvc
            .perform(get(String.format("/releases/%s", release.getId())))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    ReleaseResponse content = objectMapper.readValue(contentString, ReleaseResponse.class);

    // assert
    assertEquals(release, content);
  }

  @Test
  void getRelease_notFound_throwsException() throws Exception {
    UUID id = UUID.randomUUID();
    Exception exception =
        mockMvc
            .perform(get(String.format("/releases/%s", id)))
            .andExpect(status().isNotFound())
            .andReturn()
            .getResolvedException();
    // assert
    assertAll(
        () -> assertInstanceOf(DefaultProblem.class, exception),
        () ->
            assertEquals(
                String.format("Release with id [%s] not found", id), exception.getMessage()));
  }

  @Test
  @DataSet("artistLabelRelease.yaml")
  @ExpectedDataSet("artistLabelReleaseUpdated.yaml")
  void updateRelease_success() throws Exception {
    UUID id = TestData.releaseResponse().getId();
    ReleasePutRequest releasePutRequest = TestData.releasePutRequest();
    String contentString =
        mockMvc
            .perform(
                put(String.format("/releases/%s", id))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(releasePutRequest)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    ReleaseResponse content = objectMapper.readValue(contentString, ReleaseResponse.class);

    // assert
    assertEquals(TestData.releaseUpdatedResponse(), content);
  }

  @Test
  @DataSet("artistLabelRelease.yaml")
  @ExpectedDataSet("artistLabel.yaml")
  void deleteLabel_success() throws Exception {
    UUID id = TestData.releaseResponse().getId();
    mockMvc.perform(delete(String.format("/releases/%s", id))).andExpect(status().isOk());
  }
}
