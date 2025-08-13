package com.movilitzer.v2.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class PlaylistSource {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "venue_id")
    private Venue venue;

    @Column(nullable = false)
    private String provider; // SPOTIFY

    @Column(nullable = false)
    private String playlistId; // Spotify playlist ID

    private String displayName;

    private boolean active = true;
}
