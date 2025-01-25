package com.example.service;

import com.example.model.RuleDefinition;
import com.example.rules.JexlRule;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.api.RulesEngineParameters;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.List;

@Service
public class RulesService {
    private Rules rules;
    private RulesEngine rulesEngine;
    private final JexlEngine jexl;
    public RulesService() {
        this.jexl = new JexlBuilder()
                .strict(true)
                .silent(false)
                .create();
    }
    @PostConstruct
    public void init() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        InputStream inputStream = new ClassPathResource("rules.json").getInputStream();
        List<RuleDefinition> ruleDefinitions = mapper.readValue(inputStream, 
                new TypeReference<List<RuleDefinition>>() {});
        Rules rules = new Rules();
        for (RuleDefinition def : ruleDefinitions) {
            rules.register(new JexlRule(
                def.getName(),
                def.getDescription(),
                def.getPriority(),
                def.getCondition(),
                def.getActions(),
                jexl
            ));
        }
        this.rules = rules;
        RulesEngineParameters parameters = new RulesEngineParameters()
                .skipOnFirstAppliedRule(true);
        this.rulesEngine = new DefaultRulesEngine(parameters);
    }
    public void applyRules(Object... facts) {
        Facts ruleFacts = new Facts();
        for (Object fact : facts) {
            ruleFacts.put(fact.getClass().getSimpleName().toLowerCase(), fact);
        }
        rulesEngine.fire(rules, ruleFacts);
    }
} 