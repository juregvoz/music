package com.music.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class ReleaseResponse extends ResponseParent {

  UUID artistId;

  UUID labelId;
}
