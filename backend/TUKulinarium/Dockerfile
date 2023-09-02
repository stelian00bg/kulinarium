FROM maven:latest AS build

COPY src /usr/src/kulinarium/src

COPY pom.xml /usr/src/kulinarium

RUN mvn -f /usr/src/kulinarium/pom.xml clean package -DskipTests

FROM gcr.io/distroless/java AS kulinarium

COPY --from=build /usr/src/kulinarium/target/TUKulinarium-0.0.1-SNAPSHOT.jar /usr/kulinarium/TUKulinarium-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "/usr/kulinarium/TUKulinarium-0.0.1-SNAPSHOT.jar" ]