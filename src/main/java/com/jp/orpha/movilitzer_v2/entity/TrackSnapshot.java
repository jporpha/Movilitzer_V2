package com.jp.orpha.movilitzer_v2.entity;

import com.jp.orpha.movilitzer_v2.entity.PlaylistSource;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "track_snapshots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrackSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "playlist_source_id")
    private PlaylistSource playlistSource;

    @Column(name = "track_id", nullable = false)
    private String trackId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "artist_names", nullable = false, length = 512)
    private String artistNames;

    @Column(name = "album_name")
    private String albumName;

    @Column(name = "duration_ms")
    private Long durationMs;

    @Column(name = "spotify_url")
    private String spotifyUrl;

    @Column(name = "spotify_uri")
    private String spotifyUri;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "available", nullable = false)
    private boolean available = true;
}
