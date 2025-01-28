package com.example.models.ruleEngine.zones;

import lombok.Getter;

import java.util.List;

@Getter
public class ZoneContentRuleEngineInput {
    private int age;
    private String focusArea;
    private List<String> healthConditions;
}
