package com.music.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Label {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @NotNull private String name;

  private String description;

  @OneToMany(mappedBy = "label")
  private List<Release> releases;
}
