package com.music;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.music.entity.Label;
import com.music.repository.LabelRepository;
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
public class LabelRepositoryTest {

    @Autowired LabelRepository labelRepository;

    @BeforeEach
    @DataSet(cleanBefore = true)
    void cleanDatabase() {}

    @Test
    @ExpectedDataSet(value = "label.yaml", ignoreCols = "id")
    void saveLabel_success() {
        labelRepository.save(TestData.label());
    }

    @Test
    @DataSet(value = "label.yaml")
    void getLabel_success() {
        Optional<Label> optional =
                labelRepository.findById(UUID.fromString("82d41545-50c3-44b4-be2c-3585080985be"));

        Label label = optional.get();
        Label testLabel = TestData.label();

        assertAll(
                () -> assertEquals(testLabel.getId(), label.getId()),
                () -> assertEquals(testLabel.getName(), label.getName()),
                () -> assertEquals(testLabel.getDescription(), label.getDescription()));
    }

    @Test
    @DataSet(value = "fullDatabase.yaml")
    void getLabels_success() {
        List<Label> labels = labelRepository.findAll();
        assertEquals(2, labels.size());
    }

    @Test
    @DataSet(value = "label.yaml")
    @ExpectedDataSet(value = "labelUpdated.yaml")
    void updateLabel_success() {
        labelRepository.save(TestData.labelUpdated());
    }

    @Test
    @DataSet(value = "label.yaml")
    @ExpectedDataSet(value = "emptyDatabase.yaml")
    void deleteLabel_success() {
        labelRepository.delete(TestData.label());
    }
}
