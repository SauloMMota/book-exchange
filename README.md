# Book-Exchange

Projeto de conclusão de curso de Arquitetura de Sistemas Distribuídos - PUC-Minas.

Passos para criação dos containers.
# Criação da network
docker network create bookexchange-network

# Eureka
1) docker build --tag be-eureka .
2) docker run --name be-eureka -p 8761:8761 --network bookexchange-network be-eureka

# Gateway
1) docker build --tag be-gateway .
2) docker run --name be-gateway -p 8080:8080 -e EUREKA_SERVER=be-eureka -e KEYCLOAK_SERVER=bookexchange-keycloak -e KEYCLOAK_PORT=8080 --network bookexchange-network -d be-gateway

# Microserviço de Usuários
1) docker build --tag be-ms-usuario .
2) docker run --name be-ms-usuario --network bookexchange-network -e EUREKA_SERVER=be-eureka -e KEYCLOAK_SERVER=bookexchange-keycloak -e KEYCLOAK_PORT=8080 -d be-ms-usuario

# Microserviço de Livros
1) docker build --tag be-ms-livro .
2) docker run --name be-ms-livro --network bookexchange-network -e EUREKA_SERVER=be-eureka -e KEYCLOAK_SERVER=bookexchange-keycloak -e KEYCLOAK_PORT=8080 -d be-ms-livro

# Microserviço de Gerenciamento de Livros
1) docker build --tag be-ms-gerenciamento-livros .
2) docker run --name be-ms-gerenciamento-livros --network bookexchange-network -e RABBITMQ_SERVER=be-rabbitmq -e EUREKA_SERVER=be-eureka -e KEYCLOAK_SERVER=bookexchange-keycloak -e KEYCLOAK_PORT=8080 -d be-ms-gerenciamento-livros

# Microserviço de Notificações
1) docker build --tag be-ms-notificacoes .
2) docker run --name be-ms-notificacoes --network bookexchange-network -e RABBITMQ_SERVER=be-rabbitmq -e EUREKA_SERVER=be-eureka -e KEYCLOAK_SERVER=bookexchange-keycloak -e KEYCLOAK_PORT=8080 -d be-ms-notificacoes

# Geração do Bando de Dados de Usuários
1) docker pull postgres:12-alpine
2) docker run -p 5432:5432 --name be-bd-usuarios --network bookexchange-network -e POSTGRES_PASSWORD=123456 -e POSTGRES_DB=db_usuarios postgres:12-alpine

# Geração do Bando de Dados de Livros
1) docker run -p 5433:5432 --name be-bd-livros --network bookexchange-network -e POSTGRES_PASSWORD=123456 -e POSTGRES_DB=db_livros postgres:12-alpine

# Keycloak
1) docker run --name bookexchange-keycloak -p 8081:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin --network bookexchange-network --name bookexchange-keycloak quay.io/keycloak/keycloak:22.0.1 start-dev

# RabbitMQ
1) docker run -it --name be-rabbitmq -p 5672:5672 -p 15672:15672 --network bookexchange-network rabbitmq:3.12-management

# Importante - Ordem de Execução dos Componentes

1) Eureka
2) Gateway
3) Bancos de Dados
4) Demais microsserviços
