FROM openjdk:8-jdk-alpine
CMD ["./mvnw", "clean", "package"]
ARG JAR_FILE_PATH=target/*.jar
COPY ${JAR_FILE_PATH} board.jar
EXPOSE 8080/tcp 3306/tcp
ENTRYPOINT ["java", "-jar", "/board.jar"]