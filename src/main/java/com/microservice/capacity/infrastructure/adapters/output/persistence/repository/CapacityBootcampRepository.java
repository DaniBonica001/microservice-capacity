package com.microservice.capacity.infrastructure.adapters.output.persistence.repository;

import com.microservice.capacity.infrastructure.adapters.output.persistence.entity.CapacityBootcampEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CapacityBootcampRepository extends ReactiveCrudRepository<CapacityBootcampEntity, Integer> {
}
