package com.br.u2d.hazelcast.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.grid.ProxyManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastCustomConfig {

    private static final String BUCKET_MAP = "bucketmap";
    private static final String INSTANCE_NAME = "u2d-instance";


    @Bean
    public ProxyManager<String> bucket(HazelcastInstance hazelcastInstance) {
        return Bucket4j.extension(io.github.bucket4j.grid.hazelcast.Hazelcast.class).proxyManagerForMap(hazelcastInstance.getMap(BUCKET_MAP));
    }

    @Bean
    public HazelcastInstance hazelcastInstance(Config hazelcastConfig) {
        return com.hazelcast.core.Hazelcast.getOrCreateHazelcastInstance(hazelcastConfig);
    }

    @Bean
    public Config hazelcastConfig() {
        Config config = new Config();
        config.setNetworkConfig(networkConfig());
        config.setInstanceName(INSTANCE_NAME);
        config.addMapConfig(mapConfig());
        return config;
    }

    @Bean
    public NetworkConfig networkConfig() {
        NetworkConfig networkConfig = new NetworkConfig();
        networkConfig.setPort(8082);
        networkConfig.setPortCount(1);
        networkConfig.setPortAutoIncrement(false);
        networkConfig.setJoin(joinConfig());
        return networkConfig;
    }

    @Bean
    public JoinConfig joinConfig() {
        JoinConfig joinConfig = new JoinConfig();
        joinConfig.getMulticastConfig().setEnabled(false);
        joinConfig.getTcpIpConfig()
                .addMember("localhost").setEnabled(true);
        return joinConfig;
    }

    @Bean
    public MapConfig mapConfig() {
        MapConfig mapConfig = new MapConfig(BUCKET_MAP);
        mapConfig.setTimeToLiveSeconds(60);
        mapConfig.setMaxIdleSeconds(20);
        mapConfig.setBackupCount(0);
        return mapConfig;
    }
}
