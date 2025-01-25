package com.example.controller;

import com.example.model.Person;
import com.example.service.RulesService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rules")
public class RulesController {

    private final RulesService rulesService;

    public RulesController(RulesService rulesService) {
        this.rulesService = rulesService;
    }

    @PostMapping("/check-person")
    public Person checkPerson(@RequestBody Person person) {
        rulesService.applyRules(person);
        return person;
    }
} 