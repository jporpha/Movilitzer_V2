package com.jp.orpha.movilitzer_v2.repository;

import com.jp.orpha.movilitzer_v2.entity.TrackSnapshot;
import com.jp.orpha.movilitzer_v2.entity.PlaylistSource;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TrackSnapshotRepository extends JpaRepository<TrackSnapshot, Long> {
    List<TrackSnapshot> findByPlaylistSourceInAndAvailableTrueOrderByNameAsc(List<PlaylistSource> sources);
}