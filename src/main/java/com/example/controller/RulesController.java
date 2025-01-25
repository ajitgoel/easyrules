package com.example.controller;

import com.example.model.Person;
import com.example.service.RulesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rules")
public class RulesController {
    private final RulesService rulesService;
    private static final Logger logger = LoggerFactory.getLogger(RulesController.class);
    public RulesController(RulesService rulesService) {
        this.rulesService = rulesService;
    }

    @PostMapping("/check-person")
    public Person checkPerson(@RequestBody Person person) {
        long startTime = System.currentTimeMillis();
        rulesService.applyRules(person);
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        logger.info("Time taken to apply rules: {} ms", elapsedTime);
        return person;
    }
} 