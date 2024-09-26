package com.microservice.capacity.infrastructure.adapters.output.persistence.mapper;

import com.microservice.capacity.domain.model.CapacityBootcamp;
import com.microservice.capacity.infrastructure.adapters.output.persistence.entity.CapacityBootcampEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CapacityBootcampPersistenceMapper {

    CapacityBootcampEntity fromCapacityBootcampToCapacityBootcampEntity(CapacityBootcamp capacityBootcamp);

    List<CapacityBootcampEntity> fromCapacityBootcampToCapacityBootcampEntity(List<CapacityBootcamp> capacityBootcamps);
}
