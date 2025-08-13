package com.movilitzer.v2.repository;
import com.movilitzer.v2.entity.SpotifyLink;
import com.movilitzer.v2.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface SpotifyLinkRepository extends JpaRepository<SpotifyLink, Long> {
    Optional<SpotifyLink> findByVenue(Venue venue);
}