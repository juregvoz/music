package com.music.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ArtistInfoResponse {
    UUID id;

    String name;

    String description;

    List<ReleaseResponse> releases;
}
