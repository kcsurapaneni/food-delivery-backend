FROM maven:3.9-amazoncorretto-17 AS build

ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME

ADD ./restaurant $HOME
RUN mvn clean package -DskipTests


FROM amazoncorretto:17.0.8

COPY --from=build /usr/app/target/restaurant-0.0.1-SNAPSHOT.jar /app/runner.jar

EXPOSE 8081

ENTRYPOINT java -jar -Dspring.profiles.active=docker /app/runner.jar
