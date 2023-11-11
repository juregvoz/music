package com.music.dto;

import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class LabelInfoResponse {
    UUID id;

    String name;

    String description;

    List<ReleaseResponse> releases;
}
