package com.movilitzer.v2.service;
import com.movilitzer.v2.dto.TrackDto;
import java.util.List;

public interface SpotifyService {
    String buildAuthorizationUrl(Long venueId);
    void handleCallback(Long venueId, String code);
    List<TrackDto> syncPlaylistTracks(Long venueId);
    void addToQueue(Long venueId, String spotifyUri);
    List<TrackDto> listPublicTracks(String accessCode);
}
