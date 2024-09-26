package com.microservice.capacity.application.service;

import com.microservice.capacity.application.ports.input.CapacityServicePort;
import com.microservice.capacity.application.ports.output.CapacityPersistencePort;
import com.microservice.capacity.domain.model.Capacity;
import com.microservice.capacity.domain.model.CapacityBootcamp;
import com.microservice.capacity.domain.model.Tech_Capacity;
import com.microservice.capacity.domain.model.Technology;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CapacityService implements CapacityServicePort {
    private final CapacityPersistencePort persistencePort;
    private final WebClient webClient;

    @Override
    public Mono<Capacity> createCapacity(Capacity capacity, List<Integer> technologies) {
        return persistencePort.existsByName(capacity.getName())
                .flatMap(exists -> {
                    if (exists){
                        return Mono.error(new Exception("Capacity already exists"));
                    }

                    if(technologies.size() < 3 || technologies.size() > 20){
                        return Mono.error(new Exception("Technologies must be between 3 and 20"));
                    }

                    if(technologies.stream().distinct().count() != technologies.size()){
                        return Mono.error(new Exception("Technologies must be unique"));
                    }

                    return persistencePort.createCapacity(capacity)
                            .flatMap(createCapacity -> associateTechnologiesWithCapacity(createCapacity.getId(), technologies)
                                    .then(Mono.just(createCapacity)));
                });
    }

    @Override
    public Mono<Page<Capacity>> findAllPaged(int page, int size, String sortBy, String order) {
        return persistencePort.findAll()
                .switchIfEmpty(Mono.defer(() -> Mono.error(new Exception("Capacity not found"))))
                .flatMap(capacity -> getTechsByCapacityId(capacity.getId())
                        .collectList()
                        .map(technologies -> {
                            capacity.setTechnologies(technologies);
                            return capacity;
                        }))
                .collectList()
                .flatMap(capacities -> {
                    List<Capacity> sortedCapacities = sortCapacities(capacities, sortBy, order);
                    int start = page * size;
                    int end = Math.min(start + size, sortedCapacities.size());
                    List<Capacity> pageContent = sortedCapacities.subList(start, end);
                    return Mono.just(new PageImpl<>(pageContent, PageRequest.of(page, size), sortedCapacities.size()));
                });
    }

    @Override
    public Mono<Void> createCapacityBootcamp(int bootcampId, List<Integer> capacities) {
        return Flux.fromIterable(capacities)
                .flatMap(capacityId ->
                        existsById(capacityId)
                                .flatMap(exists ->{
                                    if (Boolean.TRUE.equals(exists)){
                                        return Mono.just(CapacityBootcamp.builder()
                                                .bootcampId(bootcampId)
                                                .capacityId(capacityId)
                                                .build());
                                    }else {
                                        return Mono.error(new Exception("Capacity with ID " + capacityId + " does not exist"));
                                    }
                                })
                                .onErrorResume(e->{
                                    log.error("Error creating capacity bootcamp: {}", e.getMessage());
                                    return Mono.empty();
                                })
                )
                .collectList()
                .flatMap(validCapacitiesBootcamp ->{
                    if (validCapacitiesBootcamp.isEmpty()){
                        return Mono.error(new Exception("No valid capacities found"));
                    }
                    return persistencePort.createCapacityBootcamp(validCapacitiesBootcamp);
                });
    }


    private Mono<Boolean> existsById(int id) {
        return persistencePort.existsById(id);
    }
    private Mono<Void> associateTechnologiesWithCapacity(int capacityId, List<Integer> technologies) {
        return webClient.post()
                .uri("/v1/api/tech_capacity")
                .bodyValue(new Tech_Capacity(capacityId, technologies))
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorResume(e -> {
                    log.error("Error associating technologies with capacity {}: {}", capacityId, e.getMessage());
                    return Mono.empty();
                });
    }

    private Flux<Technology> getTechsByCapacityId(int capacityId) {
        return webClient.get()
                .uri("/v1/api/{capacityId}", capacityId)
                .retrieve()
                .bodyToFlux(Technology.class)
                .onErrorResume(e ->{
                    log.error("Error fetching technologies for capacity {}: {}", capacityId, e.getMessage());
                    return Flux.empty();
                });
    }

    private List<Capacity> sortCapacities(List<Capacity> capacities, String sortBy, String order) {
        Comparator<Capacity> comparator;
        if ("technologies".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparingInt(c -> c.getTechnologies().size());
        } else {
            // Default to sorting by name
            comparator = Comparator.comparing(Capacity::getName);
        }

        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }

        return capacities.stream()
                .sorted(comparator)
                .toList();
    }


}
