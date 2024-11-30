package com.redis.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ConfigurationProperties(prefix = "spring.redis")
@Configuration
public class RedisProperties {

    private String host;
    private int port;
    private RedisNode master;
    private List<RedisNode> slaves;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RedisNode {
        private String host;
        private int port;
    }
}