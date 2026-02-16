# Etapa 2: Runtime
FROM eclipse-temurin:25-jdk
WORKDIR /app

# Copiar apenas o JAR final
COPY target/*.jar app.jar

# Variáveis de ambiente (parametrizáveis)
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# Porta exposta (ajusta conforme o microserviço)
EXPOSE 8181

# Comando de arranque
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]