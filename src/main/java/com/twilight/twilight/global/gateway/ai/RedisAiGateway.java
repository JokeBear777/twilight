package com.twilight.twilight.global.gateway.ai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilight.twilight.global.gateway.ai.dto.AiRecommendationPayload;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class RedisAiGateway implements AiGateway {

    private static final String STREAM_KEY   = "ai:recommend";// 스트림 이름
    private static final String GROUP_NAME   = "ai-consumers";   // 컨슈머 그룹
    private static final String PRODUCER_ID  = "spring-backend"; // 메시지 field


    private final RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
    void initGroup() {
        try {
            redisTemplate.opsForStream().createGroup(STREAM_KEY, ReadOffset.from("0-0"), GROUP_NAME);
        } catch (Exception ignored) { /* 이미 존재하면 무시 */ }
    }

    @Override
    public void send(AiRecommendationPayload payload) {
        try {
            String json = new ObjectMapper().writeValueAsString(payload);
            log.info("Payload JSON = {}", json);
        } catch (JsonProcessingException e) {
            log.error("❌ Payload 직렬화 실패", e);
        }

        Map<String, Object> body = Map.of(
                "producer", PRODUCER_ID,
                "payload", payload
        );

        redisTemplate.opsForStream().add(STREAM_KEY, body);
    }



}
