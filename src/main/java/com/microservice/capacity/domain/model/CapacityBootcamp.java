package com.microservice.capacity.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CapacityBootcamp {
    private int id;
    private int capacityId;
    private int bootcampId;
}
