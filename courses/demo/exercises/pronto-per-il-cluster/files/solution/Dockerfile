# L'immagine del servizio di saluto: il jar, eseguito da un utente senza privilegi.
FROM eclipse-temurin:25.0.4.1_1-jre-noble
WORKDIR /app
COPY saluto.jar saluto.jar
USER 10001
EXPOSE 8080
CMD ["java", "-jar", "saluto.jar"]
