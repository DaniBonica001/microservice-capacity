package com.microservice.capacity.infrastructure.adapters.input.rest.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record CapacityTechs(
    int id,
    String name,
    String description,
    List<Technology> technologies

) {
}
