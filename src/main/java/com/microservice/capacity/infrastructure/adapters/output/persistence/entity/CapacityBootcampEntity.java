package com.microservice.capacity.infrastructure.adapters.output.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("capacity_bootcamp")
public class CapacityBootcampEntity {
    @Id
    private int id;
    private int capacityId;
    private int bootcampId;
}
