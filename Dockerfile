FROM amazoncorretto:17-alpine-jdk
LABEL authors="dmitryhatsayuk"
EXPOSE 5500
COPY MoneyTransfer2-0.0.1-SNAPSHOT.jar app.jar
CMD ["java","-jar","app.jar"]

