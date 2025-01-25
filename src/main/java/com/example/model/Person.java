package com.example.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
public class Person {
    private String focusArea;
    private int isClinicalJourney;
    private String userIntent;
    private List<Zone> zones;
} 