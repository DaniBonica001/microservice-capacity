package com.microservice.capacity.application.ports.output;

import com.microservice.capacity.domain.model.Capacity;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CapacityPersistencePort {

    Mono<Capacity> createCapacity(Capacity capacity);
    Mono<Boolean> existsByName(String name);
    Flux<Capacity> findAll();
    Mono<Long> count();
}
