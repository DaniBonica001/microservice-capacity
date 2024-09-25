package com.microservice.capacity.infrastructure.adapters.output.persistence.repository;

import com.microservice.capacity.infrastructure.adapters.output.persistence.entity.CapacityEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CapacityRepository extends ReactiveCrudRepository<CapacityEntity, Integer> , ReactiveSortingRepository<CapacityEntity, Integer> {
    Mono<Boolean> existsByName(String name);
}

