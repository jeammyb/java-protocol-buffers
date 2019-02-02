FROM maven:3.6.0-jdk-8-alpine as builder
COPY . /app/simple-app
WORKDIR /app/simple-app
RUN apk add protobuf
RUN mvn clean package


FROM openjdk:8-jre-alpine as deploy
RUN apk --no-cache add ca-certificates
COPY --from=builder /app/simple-app/target /app/simple-app/scripts/launch_app.sh /app/project/
WORKDIR /app/project
CMD ["sh", "./launch_app.sh"]
