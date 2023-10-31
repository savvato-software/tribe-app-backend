package com.savvato.tribeapp.services;

import lombok.Generated;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Generated
@Service
public class SystemTimeProviderImpl implements SystemTimeProvider {

    @Override
    public Instant getCurrentInstant() {
        return Instant.now();
    }
}