package com.music.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ArtistRequest {

    @NotNull
    String name;

    String description;
}
