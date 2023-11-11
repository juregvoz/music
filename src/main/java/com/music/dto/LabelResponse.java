package com.music.dto;

import java.util.UUID;
import lombok.Data;

@Data
public class LabelResponse {

  UUID id;

  String name;

  String description;
}
