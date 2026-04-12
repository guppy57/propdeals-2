# ── Stage 1: Build ────────────────────────────────────────────────
FROM eclipse-temurin:26-jdk AS builder
WORKDIR /app

# Copy wrapper and pom first so dependency layer is cached
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

COPY src ./src
RUN ./mvnw clean package -DskipTests -B

# ── Stage 2: Run ──────────────────────────────────────────────────
FROM eclipse-temurin:26-jdk AS runner
WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

# UseContainerSupport: honour cgroup memory limits (not host RAM)
# MaxRAMPercentage: cap heap at 75% of the container's memory
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-jar", "app.jar"]
