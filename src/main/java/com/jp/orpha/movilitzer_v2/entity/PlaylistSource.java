package com.jp.orpha.movilitzer_v2.entity;

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
@Table(name = "playlist_source")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistSource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "venue_id")
    private Venue venue;

    @Column(name = "provider", nullable = false)
    private String provider; // SPOTIFY

    @Column(name = "playlist_id", nullable = false)
    private String playlistId; // Spotify playlist ID

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "active")
    private boolean active = true;
}
