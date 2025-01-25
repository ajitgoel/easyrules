package com.example.rules;

import org.apache.commons.jexl3.*;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JexlRule implements Rule {
    private final String name;
    private final String description;
    private final int priority;
    private final JexlExpression condition;
    private final List<JexlExpression> actions;
    private final JexlEngine jexl;

    public JexlRule(String name, String description, int priority, 
                    String condition, List<String> actions, JexlEngine jexl) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.jexl = jexl;
        this.condition = jexl.createExpression(condition);
        this.actions = actions.stream()
                .map(jexl::createExpression).collect(Collectors.toList());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public boolean evaluate(Facts facts) {
        JexlContext context = new MapContext();
        facts.asMap().forEach(context::set);
        try {
            Object result = condition.evaluate(context);
            return result instanceof Boolean && (Boolean) result;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void execute(Facts facts) throws Exception {
        JexlContext context = new MapContext();
        facts.asMap().forEach(context::set);
        
        for (JexlExpression action : actions) {
            action.evaluate(context);
        }
        
        // Update the facts with any modified values
        for (Map.Entry<String, Object> entry : facts.asMap().entrySet()) {
            Object newValue = context.get(entry.getKey());
            if (newValue != null && !newValue.equals(entry.getValue())) {
                facts.put(entry.getKey(), newValue);
            }
        }
    }

    @Override
    public int compareTo(Rule rule) {
        if (getPriority() < rule.getPriority()) {
            return -1;
        } else if (getPriority() > rule.getPriority()) {
            return 1;
        } else {
            return getName().compareTo(rule.getName());
        }
    }
} 