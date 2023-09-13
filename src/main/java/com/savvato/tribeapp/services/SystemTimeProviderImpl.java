package com.savvato.tribeapp.services;

import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
public class SystemTimeProviderImpl implements SystemTimeProvider {

    @Override
    public Instant getCurrentInstant() {
        return Instant.now();
    }
}