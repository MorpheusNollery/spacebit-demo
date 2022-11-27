FROM maven:latest as builder

COPY pom.xml /pom.xml
COPY src /src


RUN mvn -B -f /pom.xml dependency:resolve
ENTRYPOINT ["mvn", "clean", "test", "allure:serve"]