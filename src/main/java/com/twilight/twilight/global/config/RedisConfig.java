package com.twilight.twilight.global.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true)
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // 1) Standalone 설정
        RedisStandaloneConfiguration standalone = new RedisStandaloneConfiguration(redisHost, redisPort);

        // 2) RESP2 강제 옵션을 '빌더'에서 구성
        LettuceClientConfiguration clientConfig =
                LettuceClientConfiguration.builder()
                        .clientOptions(io.lettuce.core.ClientOptions.builder()
                                .protocolVersion(io.lettuce.core.protocol.ProtocolVersion.RESP2)
                                .build())
                        .build();

        // 3) 이 clientConfig를 넣어서 팩토리 생성
        LettuceConnectionFactory factory = new LettuceConnectionFactory(standalone, clientConfig);

        factory.setShareNativeConnection(false);
        factory.setValidateConnection(true);

        return factory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory cf) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(cf);

        GenericJackson2JsonRedisSerializer serializer =
                new GenericJackson2JsonRedisSerializer(new ObjectMapper()
                        .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                        // 여기서도 타입정보 생성을 끔
                        .deactivateDefaultTyping()
                );

        // 모든 serializer 설정
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        return template;
    }
}
