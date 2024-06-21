FROM amazoncorretto:21

COPY ../twingkling01 .

RUN chmod +x ./gradlew





#CMD ["./gradlew", "clean", "test"]
#CMD ["./gradlew", "run"]
