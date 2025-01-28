package com.example.models.ruleEngine.zones;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class ZonesRuleEngineRule {
    private String id;
    private int priority;
    private String condition;
    private ZonesRuleEngineOutput output;
}