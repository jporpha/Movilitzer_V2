package com.jp.orpha.movilitzer_v2.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Table(name = "queue_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueueItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "venue_id")
    private Venue venue;

    @Column(name = "track_id")
    private String trackId;

    @Column(name = "spotify_uri")
    private String spotifyUri;

    @ManyToOne
    @JoinColumn(name = "requested_by")
    private PublicUser requestedBy;

    @Column(name = "state", nullable = false)
    private String state; // QUEUED|PLAYING|SKIPPED|DONE

    @Column(name = "position")
    private Integer position;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "played_at")
    private LocalDateTime playedAt;

    @PrePersist
    void onCreate(){ 
        createdAt = LocalDateTime.now(); 
        if(state == null) state = "QUEUED";
    }
}
