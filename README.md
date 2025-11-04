# EasyFood API

API REST para gestão de usuários do sistema EasyFood. O projeto utiliza Spring Boot, segurança com JWT, documentação com OpenAPI/Swagger e migrações de banco com Liquibase. Há um ambiente Docker Compose com PostgreSQL e a aplicação.

## Arquitetura
- Linguagem/Runtime: Java 21 (Temurin)
- Framework: Spring Boot
  - Spring Web
  - Spring Security (JWT)
  - Spring Data JPA
- Documentação: springdoc-openapi (Swagger UI)
- Banco de dados: PostgreSQL
- Migrações: Liquibase (padrão no diretório `src/main/resources/db/changelog`)
- Mapeamento: ModelMapper
- Observabilidade/Logs: Logback
- Empacotamento e Build: Maven
- Conteinerização: Docker + Docker Compose

Fluxo de autenticação:
1. Usuário realiza login em `/api/auth/v1/login` com username e password.
2. API devolve um token JWT e o tempo de expiração.
3. As requisições protegidas devem enviar o header `Authorization: Bearer <token>`.

Regras de segurança (resumo):
- Público (sem token):
  - `POST /api/auth/v1/login`
  - `POST /api/user-system/v1` (criação de usuário)
  - Swagger: `/swagger-ui.html`, `/swagger-ui/**`, `/v3/api-docs/**`
- Protegido (com token): Demais endpoints

Porta padrão da aplicação: `8090`.

## Endpoints principais
A documentação interativa está disponível no Swagger UI: `http://localhost:8090/swagger-ui.html`.

### Autenticação
- POST `/api/auth/v1/login`
  - Request JSON: `{ "username": "usuario", "password": "senha" }`
  - Response 200: `{ "accessToken": "<jwt>", "tokenType": "Bearer", "username": "usuario", "expiresInMs": 86400000 }`
- POST `/api/auth/v1/change-password` (requer Bearer Token)
  - Request JSON: `{ "oldPassword": "senhaAtual", "newPassword": "novaSenha" }`
  - Response 204 (sem body)

### Usuários do sistema
Base path: `/api/user-system/v1`
- POST `/` (público) — cria um usuário
  - Body: `UserSystemCreateRequest`
- GET `/{id}` — obtém um usuário por id
- GET `/` — lista usuários (paginação padrão: size=10, sort=id,DESC; filtro opcional `name`)
  - Exemplo: `/api/user-system/v1?name=Igor&page=0&size=10`
- PUT `/{id}` — atualiza dados do usuário
  - Body: `UserSystemUpdateRequest`
- DELETE `/{id}` — remove um usuário

Os modelos de request/response detalhados podem ser visualizados no Swagger UI.

## Como executar com Docker Compose
Pré‑requisitos:
- Docker 24+ e Docker Compose

Arquivos relevantes:
- `Dockerfile`: build da aplicação (Java 21)
- `docker-compose.yml`: orquestração da app + PostgreSQL

Passos:
1. Clone o repositório e acesse a pasta do projeto.
2. Execute o Compose (primeira vez fará o build da imagem):
   - Linux/macOS/Windows PowerShell: `docker compose up -d --build`
3. Aguarde o banco ficar saudável e a aplicação subir.
4. Acesse: `http://localhost:8090/swagger-ui.html`

Parar os serviços:
- `docker compose down`

Logs (tempo real):
- `docker compose logs -f app`
- `docker compose logs -f postgres`

### Configurações usadas no Compose
- Serviço `postgres` (PostgreSQL 16-alpine):
  - Database: `dbeasyfood`
  - User/Password: `easyfood` / `easyfood`
  - Porta exposta: `5432`
- Serviço `app` (EasyFood):
  - Porta exposta: `8090`
  - Variáveis de ambiente:
    - `SPRING_PROFILES_ACTIVE=prd`
    - `SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/dbeasyfood`
    - `SPRING_DATASOURCE_USERNAME=easyfood`
    - `SPRING_DATASOURCE_PASSWORD=easyfood`
    - `JAVA_OPTS=-Xms256m -Xmx512m` (opcional)

Observação: O `application.yml` possui `spring.profiles.active=dev` por padrão para execução local. No Docker Compose o perfil é sobrescrito para `prd` via variável de ambiente.

## Estrutura do projeto (resumo)
- `src/main/java/br/com/imsa/easyfood/`
  - `api/controller/v1/` — controladores REST (Auth, UserSystem)
  - `api/dto/` — DTOs de requests/responses
  - `config/` — configurações (segurança, swagger, web)
  - `domain/` — entidades, serviços e providers (JWT, etc.)
  - `infrastructure/` — repositórios JPA e converters
  - `exception/` — exceções e handler global
- `src/main/resources/`
  - `application.yml` — configuração da aplicação
  - `db/changelog/` — changelog Liquibase
  - `logback.xml` — configuração de logs
- `docker-compose.yml` — orquestração Docker
- `Dockerfile` — imagem da aplicação

## Variáveis de ambiente úteis
- `SPRING_PROFILES_ACTIVE` — seleciona o perfil (dev/prd)
- `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`
- `app.jwtSecret`, `app.jwtExpirationMs` — podem ser externalizadas se necessário

## Suporte e documentação
- Swagger UI: `http://localhost:8090/swagger-ui.html`
- OpenAPI JSON (runtime): `http://localhost:8090/v3/api-docs`
- OpenAPI JSON (arquivo na raiz): [`swagger.json`](./swagger.json)
- Postman Collection (arquivo na raiz): [`postman_collection.json`](./postman_collection.json)
  - Como usar no Postman: abra o Postman > Import > selecione o arquivo `postman_collection.json` da raiz do projeto. Após importar, ajuste a variável `baseUrl` para `http://localhost:8090` (ou o host/porta em uso) e execute as requisições.
  - Alternativa: importe o `swagger.json` no Postman (Import > Link/Arquivo) para gerar uma coleção baseada no OpenAPI.
- Como visualizar o `swagger.json`: você pode abrir no [Swagger Editor](https://editor.swagger.io/) (File > Import file) ou em ferramentas como Insomnia/Stoplight.

Qualquer dúvida ou melhoria, fique à vontade para abrir uma issue ou PR.