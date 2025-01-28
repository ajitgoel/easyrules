package com.example.controller;

import reactor.core.publisher.Flux;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/{screen}/zones")
public class ZoneController {
    private final RuleEngineService ruleEngine;

    public ZoneController(RuleEngineService ruleEngine) {
        this.ruleEngine = ruleEngine;
    }

    @PostMapping
    public Flux<ZonesRuleEngineOutput> getZones(
            @PathVariable String screen,
            @RequestBody ZonesRuleEngineInput user
    ) {
        return ruleEngine.evaluateRules(user, screen);
    }
}