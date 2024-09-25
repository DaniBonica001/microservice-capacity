package com.microservice.capacity.application.service;

import com.microservice.capacity.application.ports.input.CapacityServicePort;
import com.microservice.capacity.application.ports.output.CapacityPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CapacityService implements CapacityServicePort {
    private final CapacityPersistencePort persistencePort;
}
