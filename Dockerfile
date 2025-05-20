FROM eclipse-temurin:17-jdk-jammy

WORKDIR /

# Redis 연결 대기용 스크립트 복사
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

# 필요한 netcat 설치 (Redis 대기용)
RUN apt-get update && apt-get install -y netcat && rm -rf /var/lib/apt/lists/*

# JAR 복사
COPY build/libs/twilight-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# Entrypoint 변경
ENTRYPOINT ["/entrypoint.sh"]