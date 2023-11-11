package com.music.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class ReleasePostRequest {

  @NotNull String name;

  String description;

  @NotNull UUID artistId;

  @NotNull UUID labelId;
}
