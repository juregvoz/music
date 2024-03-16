package com.music;

import com.music.dto.*;
import com.music.entity.Artist;
import com.music.entity.Label;
import com.music.entity.Release;

import java.util.List;
import java.util.UUID;

public class TestData {

    // Artist

    public static Artist artist() {
        Artist artist = new Artist();
        artist.setName("Rolling Stones");
        artist.setDescription("Some band");
        artist.setId(UUID.fromString("f0e71426-ae75-4a67-985f-d564a0458bad"));
        artist.setReleases(List.of());
        return artist;
    }

    public static Artist artistUpdated() {
        Artist artist = new Artist();
        artist.setName("Darude");
        artist.setDescription("Legendary Finnish musician.");
        artist.setId(UUID.fromString("f0e71426-ae75-4a67-985f-d564a0458bad"));
        return artist;
    }

    public static Artist artistWithRelease() {
        Artist artist = artist();
        artist.setReleases(List.of(release()));
        return artist;
    }

    public static ArtistPostRequest artistPostRequest() {
        ArtistPostRequest artistPostRequest = new ArtistPostRequest();
        artistPostRequest.setName("Rolling Stones");
        artistPostRequest.setDescription("Some band");
        return artistPostRequest;
    }

    public static ArtistResponse artistResponse() {
        ArtistResponse artist = new ArtistResponse();
        artist.setId(UUID.fromString("f0e71426-ae75-4a67-985f-d564a0458bad"));
        artist.setName("Rolling Stones");
        artist.setDescription("Some band");
        return artist;
    }

    public static ArtistInfoResponse artistInfoResponse() {
        ArtistResponse artist = artistResponse();
        ArtistInfoResponse artistInfo = new ArtistInfoResponse();
        artistInfo.setId(artist.getId());
        artistInfo.setName(artist.getName());
        artistInfo.setDescription(artist.getDescription());
        artistInfo.setReleases(List.of(releaseResponse()));
        return artistInfo;
    }

    public static ArtistPutRequest artistPutRequest() {
        ArtistPutRequest artist = new ArtistPutRequest();
        artist.setName("Darude");
        artist.setDescription("Legendary Finnish musician.");
        return artist;
    }

    public static ArtistResponse artistUpdatedResponse() {
        ArtistResponse artist = new ArtistResponse();
        artist.setId(UUID.fromString("f0e71426-ae75-4a67-985f-d564a0458bad"));
        artist.setName("Darude");
        artist.setDescription("Legendary Finnish musician.");
        return artist;
    }

    // Label

    public static Label label() {
        Label label = new Label();
        label.setId(UUID.fromString("82d41545-50c3-44b4-be2c-3585080985be"));
        label.setName("Rolling Records");
        label.setDescription("Big label");
        label.setReleases(List.of());
        return label;
    }

    public static Label labelWithRelease() {
        Label label = label();
        label.setReleases(List.of(release()));
        return label;
    }

    public static LabelPostRequest labelPostRequest() {
        LabelPostRequest labelPostRequest = new LabelPostRequest();
        labelPostRequest.setName("Rolling Records");
        labelPostRequest.setDescription("Big label");
        return labelPostRequest;
    }

    public static LabelResponse labelResponse() {
        LabelResponse label = new LabelResponse();
        label.setId(UUID.fromString("82d41545-50c3-44b4-be2c-3585080985be"));
        label.setName("Rolling Records");
        label.setDescription("Big label");
        return label;
    }

    public static LabelInfoResponse labelInfoResponse() {
        LabelResponse label = labelResponse();
        LabelInfoResponse labelInfo = new LabelInfoResponse();
        labelInfo.setId(label.getId());
        labelInfo.setName(label.getName());
        labelInfo.setDescription(label.getDescription());
        labelInfo.setReleases(List.of(releaseResponse()));
        return labelInfo;
    }

    public static LabelPutRequest labelPutRequest() {
        LabelPutRequest label = new LabelPutRequest();
        label.setName("Jugoton");
        label.setDescription("Yugoslavian record label");
        return label;
    }

    public static LabelResponse labelUpdatedResponse() {
        LabelResponse label = new LabelResponse();
        label.setId(UUID.fromString("82d41545-50c3-44b4-be2c-3585080985be"));
        label.setName("Jugoton");
        label.setDescription("Yugoslavian record label");
        return label;
    }

    // Release

    public static Release release() {
        Release release = new Release();
        release.setId(UUID.fromString("3e7c8e4f-8666-48a9-9e69-ffb8e74585c9"));
        release.setName("Gimme Shelter");
        release.setDescription("You can hear it in many Martin Scorsese's movies.");
        release.setArtist(artist());
        release.setLabel(label());
        return release;
    }

    public static ReleasePostRequest releasePostRequest() {
        ReleasePostRequest releasePostRequest = new ReleasePostRequest();
        releasePostRequest.setName("Gimme Shelter");
        releasePostRequest.setDescription("You can hear it in many Martin Scorsese's movies.");
        releasePostRequest.setLabelId(UUID.fromString("82d41545-50c3-44b4-be2c-3585080985be"));
        releasePostRequest.setArtistId(UUID.fromString("f0e71426-ae75-4a67-985f-d564a0458bad"));
        return releasePostRequest;
    }

    public static ReleaseResponse releaseResponse() {
        ReleaseResponse release = new ReleaseResponse();
        release.setId(UUID.fromString("3e7c8e4f-8666-48a9-9e69-ffb8e74585c9"));
        release.setName("Gimme Shelter");
        release.setDescription("You can hear it in many Martin Scorsese's movies.");
        release.setArtistId(UUID.fromString("f0e71426-ae75-4a67-985f-d564a0458bad"));
        release.setLabelId(UUID.fromString("82d41545-50c3-44b4-be2c-3585080985be"));
        return release;
    }

    public static ReleasePutRequest releasePutRequest() {
        ReleasePutRequest release = new ReleasePutRequest();
        release.setName("Satisfaction");
        release.setDescription("Legendary riff");
        return release;
    }

    public static ReleaseResponse releaseUpdatedResponse() {
        ReleaseResponse release = new ReleaseResponse();
        release.setId(UUID.fromString("3e7c8e4f-8666-48a9-9e69-ffb8e74585c9"));
        release.setName("Satisfaction");
        release.setDescription("Legendary riff");
        release.setArtistId(UUID.fromString("f0e71426-ae75-4a67-985f-d564a0458bad"));
        release.setLabelId(UUID.fromString("82d41545-50c3-44b4-be2c-3585080985be"));
        return release;
    }


}
