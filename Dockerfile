FROM openjdk:17-alpine
ARG JAR_FILE=build/libs/todo-*.jar
WORKDIR /opt/app
COPY ${JAR_FILE} todo.jar
ENTRYPOINT ["java","-jar","todo.jar"]

