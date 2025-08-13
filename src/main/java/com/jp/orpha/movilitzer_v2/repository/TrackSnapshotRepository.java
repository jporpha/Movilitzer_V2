package com.movilitzer.v2.repository;
import com.movilitzer.v2.entity.TrackSnapshot;
import com.movilitzer.v2.entity.PlaylistSource;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface TrackSnapshotRepository extends JpaRepository<TrackSnapshot, Long> {
    List<TrackSnapshot> findByPlaylistSourceInAndAvailableTrueOrderByNameAsc(List<PlaylistSource> sources);
}