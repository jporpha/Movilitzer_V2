package com.movilitzer.v2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class SpotifyLink {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "venue_id", nullable = false, unique = true)
    private Venue venue;

    @Column(nullable = false)
    private String ownerSpotifyUserId;

    @Column(length = 2048)
    private String accessToken;

    @Column(length = 2048)
    private String refreshToken;

    private LocalDateTime expiresAt;

    private String activeDeviceId;

    private boolean active;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate(){ createdAt = LocalDateTime.now(); }
    @PreUpdate
    void onUpdate(){ updatedAt = LocalDateTime.now(); }
}
