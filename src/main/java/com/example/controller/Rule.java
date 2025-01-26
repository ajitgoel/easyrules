package com.example.controller;

public record Rule(
        String id,
        int priority,
        String condition,
        RuleOutput output
) {}
