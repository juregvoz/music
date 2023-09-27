package com.music.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class Release {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  UUID id;

  String name;

  String description;
}
