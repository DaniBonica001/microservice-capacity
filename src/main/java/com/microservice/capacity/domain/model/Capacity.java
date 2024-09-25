package com.microservice.capacity.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Capacity {
    private int id;
    private String name;
    private String description;
    private List<Technology> technologies;
}
