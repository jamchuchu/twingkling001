FROM amazoncorretto:21

COPY . .

RUN chmod +x ./gradlew


#ENTRYPOINT ["java", "-jar", "build/libs/twingkling001-0.0.1-SNAPSHOT.jar"]



#CMD ["./gradlew", "clean", "test"]
#CMD ["./gradlew", "run"]
