FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /build

# 复制所有源码并构建 ai-agent 模块（-am 自动包含所有依赖模块）
COPY . .
RUN mvn package -pl ai-modules/ai-agent -am -DskipTests -q

# ────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

COPY --from=builder /build/ai-modules/ai-agent/target/ai-agent.jar app.jar

RUN chown appuser:appgroup app.jar
USER appuser

EXPOSE 8081

HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
    CMD wget -qO- http://localhost:8081/actuator/health || exit 1

ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar", "app.jar"]
