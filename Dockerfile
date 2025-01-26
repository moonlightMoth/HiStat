#
# BUILD STAGE
#
FROM maven:3.8.3-openjdk-17 AS build  
COPY src /usr/src/app/src  
COPY pom.xml /usr/src/app  
RUN mvn -f /usr/src/app/pom.xml clean compile package

#
# PACKAGE STAGE
#
FROM eclipse-temurin:17
COPY --from=build /usr/src/app/target/HiStat-0.1.jar /usr/app/HiStat-0.1.jar  
EXPOSE 80
CMD ["java","-jar","/usr/app/HiStat-0.1.jar", "--server.port=80"]



