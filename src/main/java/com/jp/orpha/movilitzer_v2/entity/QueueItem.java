package com.movilitzer.v2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class QueueItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "venue_id")
    private Venue venue;

    private String trackId;
    private String spotifyUri;

    @ManyToOne
    @JoinColumn(name = "requested_by")
    private PublicUser requestedBy;

    @Column(nullable = false)
    private String state; // QUEUED|PLAYING|SKIPPED|DONE

    private Integer position;
    private LocalDateTime createdAt;
    private LocalDateTime playedAt;

    @PrePersist
    void onCreate(){ 
        createdAt = LocalDateTime.now(); 
        if(state == null) state = "QUEUED";
    }
}
