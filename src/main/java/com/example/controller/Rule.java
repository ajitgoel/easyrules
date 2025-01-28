package com.example.controller;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class Rule {
    private String id;
    private int priority;
    private String condition;
    private ZonesRuleEngineOutput output;
}