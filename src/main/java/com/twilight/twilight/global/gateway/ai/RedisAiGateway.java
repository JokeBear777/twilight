package com.twilight.twilight.global.gateway.ai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilight.twilight.global.gateway.ai.dto.AiRecommendationPayload;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class RedisAiGateway implements AiGateway {

    private static final String STREAM_KEY   = "ai:recommend";// 스트림 이름
    private static final String GROUP_NAME   = "ai-consumers";   // 컨슈머 그룹
    private static final String PRODUCER_ID  = "spring-backend"; // 메시지 field


    private final RedisTemplate<String, Object> redisTemplate;



    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        try {
            ensureStreamAndGroup(STREAM_KEY, GROUP_NAME, true); // true = ReadOffset.latest()
        } catch (Exception e) {
            log.error("Redis 초기화 중 치명적 오류", e);
        }
    }


    private void ensureStreamAndGroup(String key, String group, boolean latest) {
        // 1) 스트림 존재 보장: XADD 한 건 (MKSTREAM 대용)
        try {
            var init = StreamRecords.mapBacked(Collections.singletonMap("_init", "1"))
                    .withStreamKey(key);
            redisTemplate.opsForStream().add(init);
        } catch (Exception e) {
            // XADD가 실패할 이유가 거의 없지만, 그래도 앱 죽이지 말고 로그만
            log.warn("스트림 초기 XADD 실패(무시 가능): {}", e.toString());
        }

        // 2) 그룹이 이미 있으면 생성 스킵
        try {
            var groups = redisTemplate.opsForStream().groups(key);
            if (groups != null && groups.stream().anyMatch(g -> group.equals(g.groupName()))) {
                log.info("Group already exists. key={}, group={}", key, group);
                return;
            }
        } catch (Exception e) {
            // groups 호출이 지원 안 되거나 실패해도 아래 createGroup에서 BUSYGROUP 처리하므로 계속 진행
            log.debug("groups 조회 실패(무시): {}", e.toString());
        }

        // 3) 그룹 생성 시도 (없을 때만 올 가능성 ↑)
        try {
            var offset = latest ? ReadOffset.latest() : ReadOffset.from("0-0");
            redisTemplate.opsForStream().createGroup(key, offset, group);
            log.info("Stream group created. key={}, group={}", key, group);
        } catch (Exception e) {
            if (isBusyGroup(e)) {
                // 동시 초기화/레이스 등으로 이미 만들어졌다면 성공 취급
                log.info("Group already exists (race). key={}, group={}", key, group);
                return;
            }
            // 다른 에러는 다시 던지되, init()에서 잡아 로그만 찍고 앱은 살리게 함
            throw e;
        }
    }

    private boolean isBusyGroup(Throwable e) {
        // 메시지/원인체인에 BUSYGROUP가 있으면 true
        while (e != null) {
            String msg = String.valueOf(e.getMessage());
            if (msg.contains("BUSYGROUP") || msg.contains("Consumer Group name already exists")) {
                return true;
            }
            e = e.getCause();
        }
        return false;
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
