package com.example.controller;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import org.springframework.expression.spel.standard.SpelExpressionParser;
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

    public Flux<ZonesRuleEngineOutput> evaluateRules(ZonesRuleEngineInput user, String screenName) {
        return ruleRepository.getRulesForScreen(screenName)
                .flatMapMany(rules -> Flux.fromIterable(rules)
                        .filterWhen(rule -> evaluateCondition(user, rule.getCondition()))
                        .sort(Comparator.comparingInt(Rule::getPriority))
                        .next()
                        .map(Rule::getOutput)
                        .switchIfEmpty(Mono.just(getDefaultOutput(screenName))));
    }

    private Mono<Boolean> evaluateCondition(ZonesRuleEngineInput user, String condition) {
        return Mono.fromCallable(() -> {
            EvaluationContext context = new StandardEvaluationContext(user);
            return Boolean.TRUE.equals(parser.parseExpression(condition)
                .getValue(context, Boolean.class));
        }).subscribeOn(Schedulers.boundedElastic());
    }

    private ZonesRuleEngineOutput getDefaultOutput(String screenName) {
        return switch(screenName) {
            case "nutrition-health-home" -> new ZonesRuleEngineOutput("nutrition_default",
                    List.of(Zone.builder().zoneKey("basic_health").zoneOrder(1).build()));
            default -> new ZonesRuleEngineOutput("default_intent",
                    List.of(Zone.builder().zoneKey("default_zone").zoneOrder(1).build()));
        };
    }
}