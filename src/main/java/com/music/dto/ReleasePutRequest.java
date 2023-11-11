package com.music.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ReleasePutRequest {

  String name;

  String description;

  UUID artistId;

  UUID labelId;
}
