package com.music.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class ArtistResponse {

  UUID id;

  String name;

  String description;
}
