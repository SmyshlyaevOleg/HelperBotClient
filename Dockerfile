FROM openjdk:17

# Копирование JAR-файла в контейнер
ADD /target/HelperBotClient-0.0.1-SNAPSHOT.jar bot.jar

# Команда для запуска JAR-файла
ENTRYPOINT ["java", "-jar", "bot.jar"]