package com.music.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Setter
public class Artist {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @NotNull private String name;

  private String description;

  @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
  private List<Release> releases;
}
