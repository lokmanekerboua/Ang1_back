FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy Maven configuration
COPY pom.xml .

# Download dependencies (Docker layer caching)
RUN --mount=type=cache,target=/root/.m2 \
    mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build executable JAR
RUN --mount=type=cache,target=/root/.m2 \
    mvn package -DskipTests -B && \
    cp target/*.jar /app/app.jar

FROM eclipse-temurin:21-jre

WORKDIR /app

# Run as non-root user
RUN addgroup --system spring && \
    adduser --system --ingroup spring --no-create-home --disabled-login spring

# Copy only executable JAR
COPY --from=builder --chown=spring:spring \
    /app/app.jar ./app.jar

# Run as non-root user
USER spring

# JVM optimizations
ENV JAVA_TOOL_OPTIONS="\
-XX:+UseContainerSupport \
-XX:MaxRAMPercentage=65.0 \
-XX:+UseG1GC \
-XX:+ExitOnOutOfMemoryError \
-Dfile.encoding=UTF-8"

# Application port
EXPOSE 8080

# Start Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]