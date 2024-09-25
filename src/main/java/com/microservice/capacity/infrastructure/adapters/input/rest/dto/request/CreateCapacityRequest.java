package com.microservice.capacity.infrastructure.adapters.input.rest.dto.request;

import lombok.Builder;

import java.util.List;

@Builder
public record CreateCapacityRequest(
        String name,
        String description,
        List<Integer> technologies
) {
}
