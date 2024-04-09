# Utiliser une image de base qui prend en charge Java
FROM openjdk:11-jre-slim

# Copier le jar de votre application dans le conteneur
COPY target/Examen_sem1javafx.jar /app/Examen_sem1javafx.jar

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Commande pour exécuter votre application JavaFX
CMD ["java", "-jar", "Examen_sem1javafx.jar"]
