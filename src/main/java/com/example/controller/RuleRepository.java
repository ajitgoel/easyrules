package com.example.controller;

import com.example.models.ruleEngine.zones.Rule;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RuleRepository {
    private static final Logger logger = LoggerFactory.getLogger(RuleRepository.class);
    private static final String RULE_FILE_PATTERN = "rules-%s.json";
    private final Map<String, Mono<List<Rule>>> screenRulesCache = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Mono<List<Rule>> getRulesForScreen(String screenName) {
        return screenRulesCache.computeIfAbsent(screenName, this::loadRulesForScreen);
    }

    private Mono<List<Rule>> loadRulesForScreen(String screenName) {
        return Mono.fromCallable(() -> {
            long startTime = System.currentTimeMillis();
        String fileName = String.format(RULE_FILE_PATTERN, screenName);

        try (InputStream is = getClass().getResourceAsStream("/" + fileName)) {
            if (is == null) {
                throw new RuntimeException("Rule file not found: " + fileName);
            }

            List<Rule> rules = objectMapper.readValue(is, new TypeReference<List<Rule>>() {});
            rules.sort(Comparator.comparingInt(Rule::getPriority));

            logger.info("Loaded {} rules for screen '{}' in {}ms",
                    rules.size(), screenName, System.currentTimeMillis() - startTime);

            return rules;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load rules for screen: " + screenName, e);
        }
        }).subscribeOn(Schedulers.boundedElastic()).cache();
    }

    @PostConstruct
    public void preloadCommonScreens() {
        List.of("personalized-health-home", "personalized-nutrition-home")
                .forEach(screen -> getRulesForScreen(screen).subscribe());
    }
}