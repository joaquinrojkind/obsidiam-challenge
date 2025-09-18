FROM maven:3.8.2-jdk-8
WORKDIR /obsidiam-challenge
COPY . .
RUN mvn clean install
CMD mvn spring-boot:run