package com.microservice.capacity.application.ports.input;

import com.microservice.capacity.domain.model.Capacity;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CapacityServicePort {

    Mono<Capacity> createCapacity(Capacity capacity, List<Integer> technologies);
}
