# 빌드 이미지로 OpenJDK 17 & Gradle을 지정
FROM gradle:8.10.2-jdk17 AS build

# Gradle 캐시 디렉토리 설정
ENV GRADLE_USER_HOME=/home/gradle/.gradle

# 소스 코드를 복사할 작업 디렉토리를 생성
WORKDIR /app

# 필요한 파일만 복사
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

# 종속성 캐시를 생성
RUN gradle dependencies --no-daemon

# 이후 전체 소스 복사 (캐시 미스가 발생하지 않으면 종속성 설치를 재실행하지 않음)
COPY . /app

# Gradle 빌드를 실행하여 JAR 파일 생성
RUN gradle build --no-daemon

# OpenJDK 17 기반으로 빌드
FROM openjdk:17-alpine

# 작업 디렉토리 설정
WORKDIR /app

# bash 설치 추가
RUN apk add --no-cache bash

# 루트 권한으로 변경
USER root

# wait-for-it 스크립트 복사 및 실행 권한 부여
COPY wait-for-it.sh /usr/local/bin/wait-for-it
RUN chmod +x /usr/local/bin/wait-for-it

# 빌드 이미지에서 생성된 JAR 파일을 런타임 이미지로 복사
COPY --from=build /app/build/libs/*.jar /app/ohlottery.jar

# 다시 기본 사용자로 변경
USER 1000

# 포트 노출
EXPOSE 8080

# MySQL과 Redis가 준비될 때까지 대기 후 백엔드 실행
ENTRYPOINT ["sh", "-c", "/usr/local/bin/wait-for-it mysql:3306 -- /usr/local/bin/wait-for-it redis:6379 -- java -jar /app/ohlottery.jar"]