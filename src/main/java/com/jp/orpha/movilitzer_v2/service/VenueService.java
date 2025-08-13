package com.movilitzer.v2.service;
import com.movilitzer.v2.entity.Venue;
public interface VenueService {
    Venue getByAccessCode(String code);
    Venue create(String name, String timezone);
}
