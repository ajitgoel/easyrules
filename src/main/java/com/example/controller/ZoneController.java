package com.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/{screen}/zones")
public class ZoneController {
    private final RuleEngineService ruleEngine;

    public ZoneController(RuleEngineService ruleEngine) {
        this.ruleEngine = ruleEngine;
    }

    @PostMapping
    public ResponseEntity<RuleOutput> getZones(
            @PathVariable String screen,
            @RequestBody User user
    ) {
        return ResponseEntity.ok(ruleEngine.evaluateRules(user, screen));
    }
}