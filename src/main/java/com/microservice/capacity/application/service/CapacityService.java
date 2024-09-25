package com.microservice.capacity.application.service;

import com.microservice.capacity.application.ports.input.CapacityServicePort;
import com.microservice.capacity.application.ports.output.CapacityPersistencePort;
import com.microservice.capacity.domain.model.Capacity;
import com.microservice.capacity.domain.model.Tech_Capacity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

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

    private Mono<Void> associateTechnologiesWithCapacity(int capacityId, List<Integer> technologies) {
        return webClient.post()
                .uri("/v1/api/tech_capacity")
                .bodyValue(new Tech_Capacity(capacityId, technologies))
                .retrieve()
                .bodyToMono(Void.class);
    }
}
