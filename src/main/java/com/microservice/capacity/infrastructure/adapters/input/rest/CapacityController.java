package com.microservice.capacity.infrastructure.adapters.input.rest;

import com.microservice.capacity.application.ports.input.CapacityServicePort;
import com.microservice.capacity.infrastructure.adapters.input.rest.API.CapacityAPI;
import com.microservice.capacity.infrastructure.adapters.input.rest.dto.request.CreateCapacityRequest;
import com.microservice.capacity.infrastructure.adapters.input.rest.dto.response.CapacityTechs;
import com.microservice.capacity.infrastructure.adapters.input.rest.dto.response.CreateCapacityResponse;
import com.microservice.capacity.infrastructure.adapters.input.rest.mapper.CapacityRestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class CapacityController implements CapacityAPI {
    private final CapacityServicePort servicePort;
    private final CapacityRestMapper mapper;

    @Override
    public Mono<CreateCapacityResponse> createCapacity(CreateCapacityRequest request) {
        return servicePort.createCapacity(mapper.fromCreateCapacityRequestToCapacity(request), request.technologies())
                .map(mapper::fromCapacityToCreateCapacityResponse);
    }

    @Override
    public Mono<Page<CapacityTechs>> findAll(int page, int size, String sortBy, String order) {
        return servicePort.findAllPaged(page, size, sortBy, order)
                .map(pageCapacity -> pageCapacity.map(mapper::fromCapacityToCapacityTechs));
    }


}
