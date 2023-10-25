package com.music.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class PostReleaseRequest {

  @NotNull
  String name;

  String description;

  @NotNull
  UUID artistId;
}
