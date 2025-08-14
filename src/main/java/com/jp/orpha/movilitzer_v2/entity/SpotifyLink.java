package com.jp.orpha.movilitzer_v2.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Table(name = "spotify_link")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpotifyLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "venue_id", nullable = false, unique = true)
    private Venue venue;

    @Column(name = "owner_spotify_user_id", nullable = false)
    private String ownerSpotifyUserId;

    @Column(name = "access_token", length = 2048)
    private String accessToken;

    @Column(name = "refresh_token", length = 2048)
    private String refreshToken;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "active_device_id")
    private String activeDeviceId;

    @Column(name = "active")
    private boolean active;

    @Column(name = "created_at", insertable=false, updatable=false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate(){ createdAt = LocalDateTime.now(); }
    @PreUpdate
    void onUpdate(){ updatedAt = LocalDateTime.now(); }
}
