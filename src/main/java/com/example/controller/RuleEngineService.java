package com.example.controller;

import com.example.models.ruleEngine.zones.Zone;
import com.example.models.ruleEngine.zones.Input;
import com.example.models.ruleEngine.zones.Output;
import com.example.models.ruleEngine.zones.Rule;
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

    public Flux<Output> evaluateRules(Input user, String screenName) {
        return ruleRepository.getRulesForScreen(screenName)
                .flatMapMany(rules -> Flux.fromIterable(rules)
                        .filterWhen(rule -> evaluateCondition(user, rule.getCondition()))
                        .sort(Comparator.comparingInt(Rule::getPriority))
                        .next()
                        .map(Rule::getOutput)
                        .flatMap(output -> processZones(user, output))
                        .switchIfEmpty(Mono.just(getDefaultOutput(screenName))
                                .flatMap(defaultOutput -> processZones(user, defaultOutput)))
                );
    }

    private Mono<Boolean> evaluateCondition(Input user, String condition) {
        return Mono.fromCallable(() -> {
            EvaluationContext context = new StandardEvaluationContext(user);
            return Boolean.TRUE.equals(parser.parseExpression(condition)
                    .getValue(context, Boolean.class));
        }).subscribeOn(Schedulers.boundedElastic());
    }

    private Mono<Output> processZones(Input user, Output output) {
        return Flux.fromIterable(output.getZones())
                .filterWhen(zone -> evaluateZoneCondition(user, zone))
                .collectList()
                .map(filteredZones -> new Output(output.getUserIntent(), filteredZones));
    }

    private Mono<Boolean> evaluateZoneCondition(Input user, Zone zone) {
        String condition = zone.getCondition();
        if (condition == null || condition.isEmpty()) {
            return Mono.just(true);
        } else {
            return evaluateCondition(user, condition);
        }
    }

    private Output getDefaultOutput(String screenName) {
        return switch(screenName) {
            case "nutrition-health-home" -> new Output("nutrition_default",
                    List.of(Zone.builder().zoneKey("basic_health").zoneOrder(1).build()));
            default -> new Output("default_intent",
                    List.of(Zone.builder().zoneKey("default_zone").zoneOrder(1).build()));
        };
    }
}