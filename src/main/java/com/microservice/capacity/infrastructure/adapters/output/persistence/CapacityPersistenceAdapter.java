package com.microservice.capacity.infrastructure.adapters.output.persistence;

import com.microservice.capacity.application.ports.output.CapacityPersistencePort;
import com.microservice.capacity.infrastructure.adapters.output.persistence.mapper.CapacityPersistenceMapper;
import com.microservice.capacity.infrastructure.adapters.output.persistence.repository.CapacityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CapacityPersistenceAdapter implements CapacityPersistencePort {
    private final CapacityRepository repository;
    private final CapacityPersistenceMapper mapper;
}
