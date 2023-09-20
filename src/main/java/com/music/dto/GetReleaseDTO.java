package com.music.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class GetReleaseDTO {

  public UUID id;

  public String name;

  public String description;
}
