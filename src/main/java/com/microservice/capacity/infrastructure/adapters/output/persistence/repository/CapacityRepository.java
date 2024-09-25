package com.microservice.capacity.infrastructure.adapters.output.persistence.repository;

import com.microservice.capacity.domain.model.Capacity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CapacityRepository extends ReactiveCrudRepository<Capacity, Integer> , ReactiveSortingRepository<Capacity, Integer> {

}

