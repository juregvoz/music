package com.music.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ResponseParent {

  UUID id;

  String name;

  String description;
}
