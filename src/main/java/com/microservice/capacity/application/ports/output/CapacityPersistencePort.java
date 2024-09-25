package com.microservice.capacity.application.ports.output;

import com.microservice.capacity.domain.model.Capacity;
import reactor.core.publisher.Mono;

public interface CapacityPersistencePort {

    Mono<Capacity> createCapacity(Capacity capacity);
    Mono<Boolean> existsByName(String name);
}
