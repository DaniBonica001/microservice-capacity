package com.microservice.capacity.infrastructure.adapters.input.rest.API;

import com.microservice.capacity.infrastructure.adapters.input.rest.dto.request.CreateCapacityRequest;
import com.microservice.capacity.infrastructure.adapters.input.rest.dto.response.CreateCapacityResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@RequestMapping(CapacityAPI.BASE_URL)
public interface CapacityAPI {

    String BASE_URL = "/capacity";

    @PostMapping("/v1/api")
    Mono<CreateCapacityResponse> createCapacity(@RequestBody CreateCapacityRequest request);


}
