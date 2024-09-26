package com.microservice.capacity.infrastructure.adapters.input.rest.API;

import com.microservice.capacity.infrastructure.adapters.input.rest.dto.request.CreateCapacityBootcampRequest;
import com.microservice.capacity.infrastructure.adapters.input.rest.dto.request.CreateCapacityRequest;
import com.microservice.capacity.infrastructure.adapters.input.rest.dto.response.CapacityTechs;
import com.microservice.capacity.infrastructure.adapters.input.rest.dto.response.CreateCapacityResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequestMapping(CapacityAPI.BASE_URL)
public interface CapacityAPI {

    String BASE_URL = "/capacity";

    @PostMapping("/v1/api")
    Mono<CreateCapacityResponse> createCapacity(@RequestBody CreateCapacityRequest request);

    @GetMapping("/v1/api")
    Mono<Page<CapacityTechs>> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String sortBy, @RequestParam(required = false) String order);

    @PostMapping("/v1/api/capacity_bootcamp")
    ResponseEntity<Mono<Void>> createCapacityBootcamp(@RequestBody CreateCapacityBootcampRequest request);

    @GetMapping("/v1/api/{bootcampId}")
    Flux<CapacityTechs> findByBootcampId(@PathVariable String bootcampId);

}
