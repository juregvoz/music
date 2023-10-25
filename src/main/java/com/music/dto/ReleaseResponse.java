package com.music.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ReleaseResponse {

  UUID id;

  String name;

  String description;

  UUID artistId;
}
