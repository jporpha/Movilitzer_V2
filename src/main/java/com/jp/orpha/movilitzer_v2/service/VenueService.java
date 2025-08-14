package com.jp.orpha.movilitzer_v2.service;

import com.jp.orpha.movilitzer_v2.entity.Venue;

public interface VenueService {
    Venue getByAccessCode(String code);
    Venue create(String name, String timezone);
}
