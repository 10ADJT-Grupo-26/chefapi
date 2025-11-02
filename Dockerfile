# Estágio 1: Build da Aplicação (usando o JDK)
# Usamos uma imagem JDK completa para compilar o código
FROM eclipse-temurin:21-jdk-jammy AS build

# Define o diretório de trabalho
WORKDIR /app

# Copia o Gradle Wrapper
COPY gradlew .
COPY gradle ./gradle

# Copia os arquivos de definição do build
COPY build.gradle .
COPY settings.gradle .

# Dá permissão de execução ao wrapper
RUN chmod +x ./gradlew

# Baixa as dependências primeiro para aproveitar o cache de camadas do Docker
# Se os arquivos de build não mudarem, o Docker reutiliza esta camada
RUN ./gradlew dependencies --no-daemon

# Copia o restante do código-fonte
COPY src ./src

# Constrói a aplicação (gera o .jar) pulando os testes
RUN ./gradlew bootJar --no-daemon -x test

# Estágio 2: Imagem Final (Runtime com Alpine)
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Cria um usuário não-root para rodar a aplicação
RUN adduser -D appuser

# Argumento para o caminho do JAR (no Gradle, fica em build/libs)
ARG JAR_FILE_PATH=build/libs/*.jar

# Copia o JAR construído do estágio de build para a imagem final
COPY --from=build /app/${JAR_FILE_PATH} app.jar

# Altera o proprietário e o grupo do arquivo app.jar do usuário root para o usuário appuser
RUN chown appuser:appuser app.jar

# Altera o usuário ativo para o usuário não-root
USER appuser

# Expõe a porta padrão do Spring Boot
EXPOSE 8081

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-Xmx400m", "-jar", "app.jar"]