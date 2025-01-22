FROM amazoncorretto:17-alpine-jdk
LABEL authors="dmitryhatsayuk"
EXPOSE 5500
COPY target/MoneyTransfer2-0.0.1-SNAPSHOT.jar MoneyTransfer.jar
CMD ["java","-jar","MoneyTransfer.jar"]

