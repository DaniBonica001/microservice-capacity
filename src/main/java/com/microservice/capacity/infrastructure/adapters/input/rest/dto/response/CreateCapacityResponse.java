package com.microservice.capacity.infrastructure.adapters.input.rest.dto.response;

import lombok.Builder;

@Builder
public record CreateCapacityResponse(
        int id,
        String name,
        String description
) {
}
