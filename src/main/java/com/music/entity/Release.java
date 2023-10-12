package com.music.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class Release {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  UUID id;

  @NotNull
  String name;

  String description;
}
