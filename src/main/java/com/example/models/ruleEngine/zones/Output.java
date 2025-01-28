package com.example.models.ruleEngine.zones;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public final class ZonesRuleEngineOutput {
    private String userIntent;
    private List<Zone> zones;
}