package com.music.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PostReleaseDTO {

  @NotNull
  String name;

  String description;
}
