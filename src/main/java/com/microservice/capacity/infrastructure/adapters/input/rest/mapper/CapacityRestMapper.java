package com.microservice.capacity.infrastructure.adapters.input.rest.mapper;

import com.microservice.capacity.domain.model.Capacity;
import com.microservice.capacity.infrastructure.adapters.input.rest.dto.request.CreateCapacityRequest;
import com.microservice.capacity.infrastructure.adapters.input.rest.dto.response.CapacityTechs;
import com.microservice.capacity.infrastructure.adapters.input.rest.dto.response.CreateCapacityResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CapacityRestMapper {

    @Mapping(target = "technologies", ignore = true)
    Capacity fromCreateCapacityRequestToCapacity(CreateCapacityRequest request);

    CreateCapacityResponse fromCapacityToCreateCapacityResponse(Capacity capacity);

    CapacityTechs fromCapacityToCapacityTechs(Capacity capacity);
}
