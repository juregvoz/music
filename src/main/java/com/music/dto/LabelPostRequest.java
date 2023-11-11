package com.music.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LabelPostRequest {

    @NotNull
    String name;

    String description;
}
