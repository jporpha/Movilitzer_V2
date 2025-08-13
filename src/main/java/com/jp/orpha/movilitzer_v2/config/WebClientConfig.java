package com.movilitzer.v2.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient spotifyWebClient(){
        return WebClient.builder()
                .baseUrl("https://api.spotify.com/v1")
                .build();
    }
}
