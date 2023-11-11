package com.music.service;

import com.music.dto.*;
import com.music.entity.Artist;
import com.music.repository.ArtistRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ArtistServiceImpl implements ArtistService {

  @Autowired private ArtistRepository artistRepository;

  @Autowired private ModelMapper modelMapper;

  @Override
  @Transactional
  public ArtistResponse createArtist(ArtistPostRequest dto) {
    Artist artist = artistRepository.save(modelMapper.map(dto, Artist.class));
    return modelMapper.map(artist, ArtistResponse.class);
  }

  @Override
  @Transactional
  public List<ArtistResponse> getArtists() {
    return artistRepository.findAll().stream()
        .map(artist -> modelMapper.map(artist, ArtistResponse.class))
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public ArtistInfoResponse getArtist(UUID id) {
    return modelMapper.map(findById(id), ArtistInfoResponse.class);
  }

  @Override
  @Transactional
  public Artist getArtistEntity(UUID id) {
    return findById(id);
  }

  @Override
  @Transactional
  public List<ReleaseResponse> getArtistReleases(UUID id) {
    return findById(id).getReleases().stream()
        .map(release -> modelMapper.map(release, ReleaseResponse.class))
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public ArtistResponse updateArtist(UUID id, ArtistPutRequest dto) {
    final Artist artist = findById(id);
    if (dto.getDescription() != null) {
      artist.setDescription(dto.getDescription());
    }
    if (dto.getName() != null) {
      artist.setName(dto.getName());
    }
    return modelMapper.map(artistRepository.save(artist), ArtistResponse.class);
  }

  @Override
  @Transactional
  public void deleteArtist(UUID id) {
    if (!getArtistReleases(id).isEmpty()) {
      throw Problem.builder()
          .withStatus(Status.UNPROCESSABLE_ENTITY)
          .withDetail(
              String.format("Artist with id [%s] has related releases and can't be deleted", id))
          .build();
    }
    artistRepository.delete(findById(id));
  }

  private Artist findById(UUID id) {
    return artistRepository
        .findById(id)
        .orElseThrow(
            () ->
                Problem.builder()
                    .withStatus(Status.NOT_FOUND)
                    .withDetail(String.format("Artist with id [%s] not found", id))
                    .build());
  }
}
