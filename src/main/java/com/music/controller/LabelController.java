package com.music.controller;

import com.music.dto.*;
import com.music.service.LabelService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "labels")
public class LabelController {

    @Autowired
    LabelService labelService;

    @PostMapping
    public LabelResponse createLabel(
            HttpServletResponse response, @Valid @RequestBody LabelPostRequest dto) {
        response.setStatus(HttpStatus.CREATED.value());
        return labelService.createLabel(dto);
    }

    @GetMapping
    public List<LabelResponse> getLabels() {
        return labelService.getLabels();
    }

    @GetMapping("/{id}")
    public LabelInfoResponse getLabel(@PathVariable @NotNull UUID id) {
        return labelService.getLabel(id);
    }

    @GetMapping("/{id}/releases")
    public List<ReleaseResponse> getLabelReleases(@PathVariable @NotNull UUID id) {
        return labelService.getLabelReleases(id);
    }

    @PutMapping("/{id}")
    public LabelResponse updateLabel(
            @PathVariable @NotNull UUID id, @Valid @RequestBody LabelPutRequest dto) {
        return labelService.updateLabel(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteLabel(@PathVariable @NotNull UUID id) {
        labelService.deleteLabel(id);
    }
}
