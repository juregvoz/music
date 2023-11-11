package com.music.service;

import com.music.dto.*;
import com.music.entity.Artist;
import java.util.List;
import java.util.UUID;

public interface ArtistService {
  ArtistResponse createArtist(ArtistPostRequest dto);

  List<ArtistResponse> getArtists();

  ArtistInfoResponse getArtist(UUID id);

  Artist getArtistEntity(UUID id);

  List<ReleaseResponse> getArtistReleases(UUID id);

  ArtistResponse updateArtist(UUID id, ArtistPutRequest dto);

  void deleteArtist(UUID id);
}
