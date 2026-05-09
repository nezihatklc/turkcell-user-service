# Build aşaması — uygulamayı derle
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run aşaması — sadece jar dosyasını çalıştır
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Build aşamasından jar dosyasını kopyala
COPY --from=build /app/target/*.jar app.jar

# 8080 portunu aç
EXPOSE 8080

# Uygulamayı başlat
ENTRYPOINT ["java", "-jar", "app.jar"]