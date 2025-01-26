package com.example.controller;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class RuleEngineService {
    private final RuleRepository ruleRepository;
    private final SpelExpressionParser parser = new SpelExpressionParser();

    public RuleEngineService(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    public RuleOutput evaluateRules(User user) {
        return ruleRepository.getAllRules().stream()
                .filter(rule -> evaluateCondition(user, rule.condition()))
                .max(Comparator.comparing(Rule::priority))
                .map(Rule::output)
                .orElse(new RuleOutput("default_intent",
                        List.of(new Zone("default_zone", 1))));
    }

    private boolean evaluateCondition(User user, String condition) {
        EvaluationContext context = new StandardEvaluationContext(user);
        return Boolean.TRUE.equals(parser.parseExpression(condition)
                .getValue(context, Boolean.class));
    }
}
