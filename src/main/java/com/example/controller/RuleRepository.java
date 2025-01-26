package com.example.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@Component
public class RuleRepository {
    private List<Rule> rules;

    @PostConstruct
    public void init() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = getClass().getResourceAsStream("/rules.json");
        rules = mapper.readValue(is, new TypeReference<List<Rule>>() {});
    }

    public List<Rule> getAllRules() {
        return Collections.unmodifiableList(rules);
    }
}
