package com.bankcomm.demobankcomm.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * Created with IntelliJ IDEA.
 * User: Phantom Sean
 * Date: 2025/4/10
 * Time: 15:41
 */
@Configuration
public class RemoteRedisConfig {

    @Bean(name = "remoteRedisConnectionFactory")
    public RedisConnectionFactory remoteRedisConnectionFactory(
            @Value("${remote.redis.host}") String host,
            @Value("${remote.redis.port}") int port,
            @Value("${remote.redis.password}") String password
    ) {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(host);
        config.setPort(port);
        config.setPassword(RedisPassword.of(password));
        return new LettuceConnectionFactory(config);
    }

    @Bean(name = "remoteStringRedisTemplate")
    public StringRedisTemplate remoteStringRedisTemplate(
            @Qualifier("remoteRedisConnectionFactory") RedisConnectionFactory factory
    ) {
        return new StringRedisTemplate(factory);
    }
}