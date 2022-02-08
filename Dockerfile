FROM openjdk:17-alpine
ARG JAR_FILE=build/libs/todo-0.0.1-SNAPSHOT.jar
WORKDIR /opt/app
COPY ${JAR_FILE} todo.jar
ENTRYPOINT ["java","-jar","todo.jar"]


