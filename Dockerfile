# build stage
#FROM maven:3.8-jdk-8 as build-stage
#WORKDIR /build
#COPY pom.xml ./
#COPY lib ./lib
#RUN mvn verify --fail-never
#COPY src ./src
#RUN mvn package -Dmaven.test.skip

#deploy stage
FROM openjdk:8-jdk-alpine
CMD ["./mvnw", "clean", "package"]
ARG JAR_FILE_PATH=target/*.jar
COPY ${JAR_FILE_PATH} board-0.1.jar
EXPOSE 8080/tcp
ENTRYPOINT ["java", "-jar", "/board-0.1.jar"]

# docker build -t docker-board:0.0.1 .
# docker run docker-board:0.0.1
# docker images
# docker run docker-board:0.0.1

# docker run -it -p 8080:8080 docker-baord-0.0.1 과 Expose 8080 차이
# Dockerfile에서 Expose 한다고 해서 호스트의 포트와 매핑되지는 않는다.
# Expose된 포트는 'docker run -P' 옵션일때 매핑된다.
# docker run -p 와 docker run -P 옵션비교하면
# -p는 직접 매핑할 포트를 지정해야하고, -P옵션은 운영체제의 임의의 포트번호가 노출된 컨테이너의 포트로 매핑된다.

# Local Dockerize Process
# 소스코드가 업데이트 되면 mvn package 를 통해서 원본 패키징 jar 생성
# Dockerfile 이 있는 디렉토리에서 다음 명령어 실행
# docker build
# docker run -p

# 서버에서 접속할때도 결국 jar 파일만 존재하면 build 하고 run 할수 있다.



