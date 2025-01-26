package com.example.controller;

import java.util.List;

public record RuleOutput(
        String userIntent,
        List<Zone> zones
){}
