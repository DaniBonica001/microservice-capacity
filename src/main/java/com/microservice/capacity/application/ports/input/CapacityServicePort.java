package com.microservice.capacity.application.ports.input;

import com.microservice.capacity.domain.model.Capacity;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CapacityServicePort {

    Mono<Capacity> createCapacity(Capacity capacity, List<Integer> technologies);
    Mono<Page<Capacity>> findAllPaged(int page, int size, String sortBy, String order);
    Mono<Void> createCapacityBootcamp(int bootcampId, List<Integer> capacities);
}
