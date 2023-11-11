package com.music.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ArtistInfoResponse extends ResponseParent {

  List<ReleaseResponse> releases;
}
