package com.br.u2d.hazelcast.service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.EstimationProbe;
import io.github.bucket4j.grid.GridBucketState;
import io.github.bucket4j.grid.RecoveryStrategy;
import io.github.bucket4j.grid.hazelcast.Hazelcast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimitService {

    private static final String BUCKET_MAP = "bucketmap";

    @Autowired
    HazelcastInstance hazelcastInstance;

    public EstimationProbe estimateAbilityToConsumeBucket(String apiKey) {
        return getBucket(apiKey).estimateAbilityToConsume(1);
    }

    public Bucket getBucket(String apiKey) {
        IMap<String, GridBucketState> map = hazelcastInstance.getMap(BUCKET_MAP);
        Bandwidth limit = Bandwidth.simple(10, Duration.ofMinutes(1));
        Bucket bucket = Bucket4j.extension(Hazelcast.class).builder()
                .addLimit(limit)
                .build(map, apiKey, RecoveryStrategy.RECONSTRUCT);
        return bucket;
    }
}
