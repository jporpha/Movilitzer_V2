package com.movilitzer.v2.service.impl;

import com.movilitzer.v2.entity.Venue;
import com.movilitzer.v2.exception.BadRequestException;
import com.movilitzer.v2.repository.VenueRepository;
import com.movilitzer.v2.service.VenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VenueServiceImpl implements VenueService {

    private final VenueRepository venueRepository;

    @Override
    public Venue getByAccessCode(String code) {
        return venueRepository.findByAccessCode(code)
                .orElseThrow(() -> new BadRequestException("Código de acceso inválido"));
    }

    @Override
    public Venue create(String name, String timezone) {
        String access = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        Venue v = Venue.builder().name(name).timezone(timezone).status("ACTIVE").accessCode(access).build();
        return venueRepository.save(v);
    }
}
