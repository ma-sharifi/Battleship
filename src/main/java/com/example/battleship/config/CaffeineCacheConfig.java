package com.example.battleship.config;

import com.example.battleship.model.BattleshipGame;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author Mahdi Sharifi
 */
@Configuration
public class CaffeineCacheConfig {

    /**
     * This is the main configuration that will control caching behavior such as expiration,
     * cache size limits, and more
     */
    @Bean
    public Cache<String, BattleshipGame> caffeineConfig() {
        return Caffeine
                .newBuilder()
                .initialCapacity(10)
                .maximumSize(50)
                .softValues() //remove the objects before OOM Error
                .expireAfterAccess(1, TimeUnit.HOURS).build();
    }

}
