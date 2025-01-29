package com.example.controller;

import com.example.models.ruleEngine.zones.Input;
import com.example.models.ruleEngine.zones.Output;
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
    public Flux<Output> getZones(
            @PathVariable String screen,
            @RequestBody Input user
    ) {
        return ruleEngine.evaluateRules(user, screen);
    }
}