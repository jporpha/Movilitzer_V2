package com.movilitzer.v2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class PublicUser {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String anonKey;
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate(){ createdAt = LocalDateTime.now(); }
}
