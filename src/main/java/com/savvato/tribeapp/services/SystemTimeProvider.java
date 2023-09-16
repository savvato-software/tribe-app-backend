package com.savvato.tribeapp.services;

import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public interface SystemTimeProvider {
    Instant getCurrentInstant();
}
