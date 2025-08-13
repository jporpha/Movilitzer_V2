package com.movilitzer.v2.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class TrackSnapshot {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "playlist_source_id")
    private PlaylistSource playlistSource;

    @Column(nullable = false)
    private String trackId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 512)
    private String artistNames;

    private String albumName;

    private Long durationMs;

    private String spotifyUrl;
    private String spotifyUri;
    private String imageUrl;

    @Column(nullable = false)
    private boolean available = true;
}
