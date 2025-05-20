#!/bin/sh
echo "Waiting for Redis to be available at $SPRING_REDIS_HOST:$SPRING_REDIS_PORT..."

until nc -z "$SPRING_REDIS_HOST" "$SPRING_REDIS_PORT"; do
  echo "Redis is unavailable - sleeping"
  sleep 1
done

echo "Redis is up - executing application"
exec java -jar /app.jar