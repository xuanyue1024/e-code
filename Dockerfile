FROM eclipse-temurin:17-jre-jammy

# 设置时区为 Asia/Shanghai（中国标准时间）
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone

WORKDIR /app
COPY e-server/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]