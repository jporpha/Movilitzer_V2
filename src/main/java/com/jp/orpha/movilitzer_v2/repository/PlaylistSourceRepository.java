package com.jp.orpha.movilitzer_v2.repository;

import com.jp.orpha.movilitzer_v2.entity.PlaylistSource;
import com.jp.orpha.movilitzer_v2.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlaylistSourceRepository extends JpaRepository<PlaylistSource, Long> {
    List<PlaylistSource> findByVenueAndActiveTrue(Venue venue);
}