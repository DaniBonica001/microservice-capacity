package com.microservice.capacity.infrastructure.adapters.output.persistence.mapper;

import com.microservice.capacity.domain.model.Capacity;
import com.microservice.capacity.infrastructure.adapters.output.persistence.entity.CapacityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CapacityPersistenceMapper {

    CapacityEntity fromCapacityToCapacityEntity(Capacity capacity);
    Capacity fromCapacityEntityToCapacity(CapacityEntity capacityEntity);
}
