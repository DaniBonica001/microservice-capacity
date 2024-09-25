package com.microservice.capacity.infrastructure.adapters.input.rest.mapper;

import com.microservice.capacity.domain.model.Capacity;
import com.microservice.capacity.infrastructure.adapters.input.rest.dto.request.CreateCapacityRequest;
import com.microservice.capacity.infrastructure.adapters.input.rest.dto.response.CreateCapacityResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CapacityRestMapper {

    Capacity fromCreateCapacityRequestToCapacity(CreateCapacityRequest request);

    CreateCapacityResponse fromCapacityToCreateCapacityResponse(Capacity capacity);
}
