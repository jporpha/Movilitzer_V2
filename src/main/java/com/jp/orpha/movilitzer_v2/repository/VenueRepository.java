package com.jp.orpha.movilitzer_v2.repository;

import com.jp.orpha.movilitzer_v2.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VenueRepository extends JpaRepository<Venue, Long> {
    Optional<Venue> findByAccessCode(String accessCode);
}