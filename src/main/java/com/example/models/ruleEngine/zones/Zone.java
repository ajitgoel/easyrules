package com.example.models.ruleEngine.zones;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public final class Zone {
    private String zoneKey;
    private int zoneOrder;
    @JsonIgnore
    private String condition;
}