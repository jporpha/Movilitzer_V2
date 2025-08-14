package com.jp.orpha.movilitzer_v2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Table(name = "public_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublicUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "anon_key")
    private String anonKey;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate(){ createdAt = LocalDateTime.now(); }
}
