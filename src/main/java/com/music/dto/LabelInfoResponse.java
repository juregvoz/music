package com.music.dto;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LabelInfoResponse extends ResponseParent {

  List<ReleaseResponse> releases;
}
