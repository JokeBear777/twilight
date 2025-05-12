package com.twilight.twilight.global.gateway.ai;

import com.twilight.twilight.global.gateway.ai.dto.AiRecommendationPayload;

public interface AiGateway {

    void send(
        AiRecommendationPayload aiRecommendationPayload
    );

}
