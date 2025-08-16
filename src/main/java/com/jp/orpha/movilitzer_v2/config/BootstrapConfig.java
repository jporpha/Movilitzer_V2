package com.jp.orpha.movilitzer_v2.config;

import com.jp.orpha.movilitzer_v2.entity.PlaylistSource;
import com.jp.orpha.movilitzer_v2.entity.Venue;
import com.jp.orpha.movilitzer_v2.repository.PlaylistSourceRepository;
import com.jp.orpha.movilitzer_v2.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BootstrapConfig {

    private final VenueRepository venueRepository;
    private final PlaylistSourceRepository playlistSourceRepository;

    @Bean
    CommandLineRunner seed(){
        return args -> {
            if(venueRepository.count() == 0){
                Venue v = Venue.builder()
                        .name(System.getProperty("MOV_BOOTSTRAP_VENUE","Mi Primer Local"))
                        .timezone("America/Santiago")
                        .status("ACTIVE")
                        .accessCode("ABC123")
                        .build();
                v = venueRepository.save(v);

                String playlistId = System.getProperty("MOV_BOOTSTRAP_PLAYLIST","37i9dQZF1DXcBWIGoYBM5M");
                PlaylistSource ps = PlaylistSource.builder()
                        .venue(v).provider("SPOTIFY")
                        .playlistId(playlistId)
                        .displayName("Playlist de prueba")
                        .active(true).build();
                playlistSourceRepository.save(ps);
            }
        };
    }
}