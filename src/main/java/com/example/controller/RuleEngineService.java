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
    public RuleOutput evaluateRules(User user, String screenName) {
        return ruleRepository.getRulesForScreen(screenName).stream()
                .filter(rule -> evaluateCondition(user, rule.condition()))
                .findFirst()
                .map(Rule::output)
                .orElseGet(() -> getDefaultOutput(screenName));
    }

    private RuleOutput getDefaultOutput(String screenName) {
        return switch(screenName) {
            case "nutrition-health-home" -> new RuleOutput("nutrition_default",
                    List.of(new Zone("basic_nutrition", 1)));
            default -> new RuleOutput("default_intent",
                    List.of(new Zone("default_zone", 1)));
        };
    }
    private boolean evaluateCondition(User user, String condition) {
        EvaluationContext context = new StandardEvaluationContext(user);
        return Boolean.TRUE.equals(parser.parseExpression(condition)
                .getValue(context, Boolean.class));
    }
}

