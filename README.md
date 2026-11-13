# bloco-de-notas-backend

API REST de bloco de notas — Quarkus 3 / Java 21 (Etapa 1 da Trilha Backend).

## Rodar
./mvnw quarkus:dev
Swagger UI: http://localhost:8080/q/swagger-ui

## Testes
./mvnw test

## Rotas
- /usuarios — GET, POST, PUT, DELETE
- /api/notes — GET, POST, PUT, DELETE (inclui /user/{userId})