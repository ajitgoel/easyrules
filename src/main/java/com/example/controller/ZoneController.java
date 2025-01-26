package com.example.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/zones")
public class ZoneController {
    private final RuleEngineService ruleEngine;

    public ZoneController(RuleEngineService ruleEngine) {
        this.ruleEngine = ruleEngine;
    }

    @PostMapping
    public ResponseEntity<RuleOutput> getZones(@RequestBody User user) {
        return ResponseEntity.ok(ruleEngine.evaluateRules(user));
    }
}
//@SpringBootTest
//class RuleEngineApplicationTests {
//    @Autowired
//    RuleEngineService ruleEngine;
//
//    @Test
//    void testFitnessRule() {
//        User user = new User();
//        user.setAge(25);
//        user.setFocusArea("fitness");
//        user.setHealthConditions(List.of());
//
//        RuleOutput output = ruleEngine.evaluateRules(user);
//
//        assertEquals("fitness_tracking", output.userIntent());
//        assertTrue(output.zones().stream()
//                .anyMatch(z -> z.zoneKey().equals("gym_workouts")));
//    }
//}