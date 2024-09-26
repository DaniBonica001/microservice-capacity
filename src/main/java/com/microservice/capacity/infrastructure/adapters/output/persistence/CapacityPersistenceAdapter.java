package com.microservice.capacity.infrastructure.adapters.output.persistence;

import com.microservice.capacity.application.ports.output.CapacityPersistencePort;
import com.microservice.capacity.domain.model.Capacity;
import com.microservice.capacity.domain.model.CapacityBootcamp;
import com.microservice.capacity.infrastructure.adapters.output.persistence.mapper.CapacityBootcampPersistenceMapper;
import com.microservice.capacity.infrastructure.adapters.output.persistence.mapper.CapacityPersistenceMapper;
import com.microservice.capacity.infrastructure.adapters.output.persistence.repository.CapacityBootcampRepository;
import com.microservice.capacity.infrastructure.adapters.output.persistence.repository.CapacityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CapacityPersistenceAdapter implements CapacityPersistencePort {
    private final CapacityRepository repository;
    private final CapacityBootcampRepository capacityBootcampRepository;
    private final CapacityPersistenceMapper mapper;
    private final CapacityBootcampPersistenceMapper capacityBootcampPersistenceMapper;

    @Override
    public Mono<Capacity> createCapacity(Capacity capacity) {
        return repository.save(mapper.fromCapacityToCapacityEntity(capacity)).map(mapper::fromCapacityEntityToCapacity);
    }

    @Override
    public Mono<Boolean> existsByName(String name) {
        return repository.existsByName(name);
    }

    @Override
    public Flux<Capacity> findAll() {
        return repository.findAll().map(mapper::fromCapacityEntityToCapacity);
    }

    @Override
    public Mono<Long> count() {
        return repository.count();
    }

    @Override
    public Mono<Boolean> existsById(int id) {
        return repository.existsById(id);
    }

    @Override
    public Mono<Void> createCapacityBootcamp(List<CapacityBootcamp> capacities) {
        return capacityBootcampRepository.saveAll(capacityBootcampPersistenceMapper.fromCapacityBootcampToCapacityBootcampEntity(capacities)).then();
    }
}
