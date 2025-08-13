package com.movilitzer.v2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Vote {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "queue_item_id")
    private QueueItem queueItem;

    @ManyToOne(optional = false)
    @JoinColumn(name = "public_user_id")
    private PublicUser publicUser;

    private Integer value; // +1

    private LocalDateTime createdAt;

    @PrePersist
    void onCreate(){ createdAt = LocalDateTime.now(); }
}
