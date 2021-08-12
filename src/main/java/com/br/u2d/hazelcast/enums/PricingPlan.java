package com.br.u2d.hazelcast.enums;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PricingPlan {

    private String apiKey;

    public Bandwidth resolvePlanFromApiKey() {
        if (apiKey == null || apiKey.isEmpty()) {
            return Bandwidth.classic(3, Refill.intervally(3, Duration.ofHours(1)));
        } else if (apiKey.startsWith("PX001-")) {
            return Bandwidth.classic(20, Refill.intervally(10, Duration.ofHours(1)));
        } else if (apiKey.startsWith("BX001-")) {
            return Bandwidth.classic(10, Refill.intervally(10, Duration.ofHours(1)));
        }
        return Bandwidth.classic(3, Refill.intervally(3, Duration.ofHours(1)));
    }
}
