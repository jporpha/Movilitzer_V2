package com.jp.orpha.movilitzer_v2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Table(name = "votes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "queue_item_id")
    private QueueItem queueItem;

    @ManyToOne(optional = false)
    @JoinColumn(name = "public_user_id")
    private PublicUser publicUser;

    @Column(name = "score", nullable = false) // <- renombrado (antes "value")
    private Integer score; // +1

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate(){ createdAt = LocalDateTime.now(); }
}
