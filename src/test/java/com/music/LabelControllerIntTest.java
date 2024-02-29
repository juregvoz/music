package com.music;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.music.dto.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
public class LabelControllerIntTest {

  @Autowired MockMvc mockMvc;

  @Autowired ObjectMapper objectMapper;

  @BeforeEach
  @DataSet(cleanBefore = true)
  void cleanDatabase() {}

  @Test
  @ExpectedDataSet(value = "label.yaml", ignoreCols = "id")
  void createLabel_success() throws Exception {
    LabelPostRequest labelPostRequest = TestData.labelPostRequest();
    String contentString =
        mockMvc
            .perform(
                post("/labels")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(labelPostRequest)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    LabelResponse content = objectMapper.readValue(contentString, LabelResponse.class);

    // assert
    assertAll(
        () -> Assertions.assertEquals(labelPostRequest.getDescription(), content.getDescription()),
        () -> Assertions.assertEquals(labelPostRequest.getName(), content.getName()),
        () -> assertInstanceOf(UUID.class, content.getId()));
  }

  @Test
  void createLabel_noName_throwsException() throws Exception {
    LabelPostRequest labelPostRequest = new LabelPostRequest();
    MvcResult result =
        mockMvc
            .perform(
                post("/labels")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(labelPostRequest)))
            .andExpect(status().isBadRequest())
            .andReturn();

    // assert
    assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException());
  }

  @Test
  @DataSet("fullDatabase.yaml")
  void getLabels_success() throws Exception {
    String contentString =
        mockMvc
            .perform(get("/labels"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    List<LabelResponse> content = objectMapper.readValue(contentString, new TypeReference<>() {});

    // assert
    assertEquals(2, content.toArray().length);
  }

  @Test
  @DataSet("fullDatabase.yaml")
  void getLabel_success() throws Exception {
    LabelInfoResponse label = TestData.labelInfoResponse();
    String contentString =
        mockMvc
            .perform(get(String.format("/labels/%s", label.getId())))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    LabelInfoResponse content = objectMapper.readValue(contentString, LabelInfoResponse.class);

    // assert
    assertEquals(label, content);
  }

  @Test
  @DataSet("emptyDatabase.yaml")
  void getLabel_notFound_throwsException() throws Exception {
    UUID id = UUID.fromString("74377b54-67f5-4469-b7fe-d225d7d3902e");

    Exception exception =
        mockMvc
            .perform(get(String.format("/labels/%s", id)))
            .andExpect(status().isNotFound())
            .andReturn()
            .getResolvedException();

    // assert
    assertAll(
        () -> assertInstanceOf(DefaultProblem.class, exception),
        () ->
            assertEquals(
                String.format("Label with id [%s] not found", id), exception.getMessage()));
  }

  @Test
  @DataSet("label.yaml")
  @ExpectedDataSet("labelUpdated.yaml")
  void updateLabel_success() throws Exception {
    UUID id = TestData.labelResponse().getId();
    LabelPutRequest labelPutRequest = TestData.labelPutRequest();
    String contentString =
        mockMvc
            .perform(
                put(String.format("/labels/%s", id))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(labelPutRequest)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    LabelResponse content = objectMapper.readValue(contentString, LabelResponse.class);

    assertEquals(TestData.labelUpdatedResponse(), content);
  }

  @Test
  @DataSet("label.yaml")
  @ExpectedDataSet("emptyDatabase.yaml")
  void deleteLabel_success() throws Exception {
    UUID id = TestData.labelResponse().getId();
    mockMvc.perform(delete(String.format("/labels/%s", id))).andExpect(status().isOk());
  }

  @Test
  @DataSet("fullDatabase.yaml")
  void deleteLabel_hasRelatedReleases_throwsException() throws Exception {
    UUID id = TestData.labelResponse().getId();
    Exception exception =
        mockMvc
            .perform(delete(String.format("/labels/%s", id)))
            .andExpect(status().isUnprocessableEntity())
            .andReturn()
            .getResolvedException();

    assertAll(
        () -> assertInstanceOf(DefaultProblem.class, exception),
        () ->
            assertEquals(
                String.format("Label with id [%s] has related releases and can't be deleted", id),
                exception.getMessage()));
  }
}
