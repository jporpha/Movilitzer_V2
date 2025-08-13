package com.movilitzer.v2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Venue {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 80)
    private String name;

    @Column(nullable = false, unique = true, length = 12)
    private String accessCode;

    @Column(nullable = false)
    private String timezone;

    @Column(nullable = false)
    private String status; // ACTIVE, INACTIVE

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate(){ createdAt = LocalDateTime.now(); }
    @PreUpdate
    void onUpdate(){ updatedAt = LocalDateTime.now(); }
}
