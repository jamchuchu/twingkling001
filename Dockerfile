FROM amazoncorretto:21

COPY . .

RUN chmod +x ./gradlew





#CMD ["./gradlew", "clean", "test"]
#CMD ["./gradlew", "run"]
