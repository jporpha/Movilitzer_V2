package com.jp.orpha.movilitzer_v2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "venues")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 80)
    private String name;

    @Column(name = "access_code", nullable = false, unique = true, length = 12)
    private String accessCode;

    @Column(name = "timezone", nullable = false)
    private String timezone;

    @Column(name = "status", nullable = false)
    private String status; // ACTIVE, INACTIVE


    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "update_at")
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate(){ createdAt = LocalDateTime.now(); }
    @PreUpdate
    void onUpdate(){ updatedAt = LocalDateTime.now(); }
}
