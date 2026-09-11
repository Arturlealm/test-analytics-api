# Analytics API

API backend desenvolvida em Java com Spring Boot para gerenciamento de aplicações, usuários finais e eventos de analytics.

O projeto permite registrar aplicações, gerar chaves de API, receber eventos de uso e consultar dados através de endpoints protegidos por autenticação JWT.

## Documentação da API

A documentação dos endpoints pode ser visualizada através do Swagger UI:

**[Acessar Swagger UI](https://arturlealm.github.io/test-analytics-api/)**

> A versão publicada no GitHub Pages permite visualizar a documentação da API sem a necessidade de executar o projeto localmente.

## Tecnologias utilizadas

- Java 25
- Spring Boot 4
- Gradle
- PostgreSQL
- Spring Data JPA
- Spring Security
- JWT
- Liquibase
- Docker
- Docker Compose
- Swagger / OpenAPI

## Funcionalidades principais

- Cadastro e consulta de aplicações
- Geração e gerenciamento de API Keys
- Cadastro e consulta de usuários finais
- Registro de eventos de funcionalidades
- Registro de consumo de IA
- Filtros e paginação nas consultas
- Autenticação JWT para endpoints administrativos
- Autenticação via `X-Api-Key` para ingestão de dados
- Documentação da API com Swagger
- Versionamento do banco com Liquibase

## Executando o projeto

### Usando Docker

Com Docker e Docker Compose instalados:

```bash
docker compose up --build
```

A aplicação ficará disponível em:

`http://localhost:8080`

O PostgreSQL ficará disponível localmente na porta `5433`.

### Executando localmente

O banco pode ser iniciado separadamente com:

```bash
docker compose up postgres
```

Depois execute a aplicação:

```bash
./gradlew bootRun
```

No Windows:

```powershell
.\gradlew.bat bootRun
```

## Variáveis de ambiente

Exemplo:

```env
DB_URL=jdbc:postgresql://localhost:5433/analytics
DB_USER=postgres
DB_PASSWORD=postgres
JWT_SECRET=change-this-secret-change-this-secret
JWT_EXPIRATION=3600
```

## Banco de dados

As tabelas são criadas automaticamente através do Liquibase ao iniciar a aplicação.

O projeto possui migrations para:

- aplicações
- API Keys
- usuários finais
- eventos de funcionalidades
- usos de IA
- usuários do dashboard

## Autenticação

Existem dois tipos de autenticação.

### JWT

Utilizado para acessar os endpoints administrativos e de consulta.

Endpoint de login:

`POST /v1/auth/tokens`

Usuário padrão criado através do seed:

- E-mail: `admin@analytics.local`
- Senha: `admin123`

Após o login, utilize o token recebido:

`Authorization: Bearer SEU_TOKEN`

### API Key

Os endpoints responsáveis pela ingestão de dados utilizam:

`X-Api-Key: SUA_API_KEY`

A chave é exibida no momento da criação e armazenada no banco somente através de seu hash.

## Principais endpoints

## Exemplo de uso

Abaixo está um exemplo simples do fluxo principal da API.
> Os exemplos abaixo utilizam Bash. No Windows, os comandos podem ser executados através do Git Bash.

### 1. Realizar login

Primeiro, obtenha um token JWT utilizando o usuário administrador:

```bash
curl -X POST http://localhost:8080/v1/auth/tokens \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@analytics.local",
    "password": "admin123"
  }'
```

A resposta contém o token que será utilizado nos endpoints administrativos.

### 2. Criar uma aplicação

Utilize o token JWT retornado no login:

```bash
curl -X POST http://localhost:8080/v1/applications \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN" \
  -d '{
    "name": "Minha Aplicacao",
    "description": "Aplicacao utilizada como exemplo"
  }'
```

Guarde o `id` retornado pela aplicação.

### 3. Gerar uma API Key

Com o identificador da aplicação criada:

```bash
curl -X POST http://localhost:8080/v1/applications/APPLICATION_ID/api-keys \
  -H "Authorization: Bearer SEU_TOKEN"
```

A resposta contém a API Key no campo `key`.

A chave deve ser guardada nesse momento, pois posteriormente somente seu hash permanece armazenado no banco.

### 4. Enviar dados para a API

Os endpoints de ingestão utilizam a API Key através do header `X-Api-Key`.

Exemplo de criação de um usuário final:

```bash
curl -X POST http://localhost:8080/v1/end-users \
  -H "Content-Type: application/json" \
  -H "X-Api-Key: SUA_API_KEY" \
  -d '{
    "externalId": "user-001",
    "ageRange": "25_34",
    "region": "PE"
  }'
```

Esse mesmo modelo de autenticação por API Key é utilizado nos endpoints de ingestão de Feature Events e AI Usages.

### Autenticação

`POST /v1/auth/tokens`

### Aplicações

- `POST /v1/applications`
- `GET /v1/applications`
- `GET /v1/applications/{id}`

### API Keys

- `POST /v1/applications/{applicationId}/api-keys`
- `GET /v1/applications/{applicationId}/api-keys`

### Usuários finais

- `POST /v1/end-users`
- `GET /v1/end-users`
- `GET /v1/end-users/{id}`
- `DELETE /v1/end-users/{id}`

### Feature Events

- `POST /v1/feature-events`
- `GET /v1/feature-events`

### AI Usages

- `POST /v1/ai-usages`
- `GET /v1/ai-usages`

## Paginação e filtros

As consultas podem utilizar parâmetros como:

- `page`
- `limit`
- `sort`
- `order`

Além de filtros específicos para cada recurso.

Exemplo:

`GET /v1/feature-events?page=1&limit=20&sort=occurredAt&order=desc`

## Swagger / OpenAPI

A documentação interativa está disponível em:

`http://localhost:8080/docs/swagger`

A especificação OpenAPI em JSON está disponível em:

`http://localhost:8080/docs/openapi`

## Estrutura do projeto

O projeto foi organizado em camadas:

- `controller`: recebe as requisições HTTP
- `service`: contém as regras da aplicação
- `repository`: comunicação com o banco
- `entity`: representação das tabelas
- `dto`: objetos utilizados nas requisições e respostas
- `specification`: filtros dinâmicos das consultas
- `exception`: tratamento de erros
- `config`: configurações de segurança e da aplicação
- `util`: classes utilitárias utilizadas pelo projeto

## Decisões técnicas

Durante o desenvolvimento foram adotadas algumas decisões para manter a API organizada e próxima de um cenário real de uso:

- Utilização de UUIDv7 para os identificadores das entidades.
- PostgreSQL como banco de dados relacional.
- Liquibase para criação e versionamento da estrutura do banco.
- JWT para autenticação dos endpoints administrativos e de consulta.
- API Keys para autenticação dos endpoints responsáveis pela ingestão de dados.
- Armazenamento apenas do hash das API Keys.
- Specifications do Spring Data JPA para construção dos filtros dinâmicos.
- Paginação nas consultas que podem retornar múltiplos registros.
- `ProblemDetail` para padronização das respostas de erro.
- Docker e Docker Compose para facilitar a execução do ambiente.


Devido ao prazo disponível para o desafio, foram priorizados os fluxos principais e os requisitos funcionais da API.

Como melhorias futuras, poderiam ser adicionados testes de integração completos utilizando Testcontainers com PostgreSQL, 
maior cobertura automatizada dos endpoints e validações adicionais nos parâmetros de paginação e ordenação.

Os itens opcionais e bônus não foram priorizados durante a implementação.

## Observações

- O projeto utiliza UUID versão 7 como identificador das entidades.
  Para sua geração foi utilizada a biblioteca `uuid-creator`, já que o UUIDv7 não é gerado nativamente pela estrutura utilizada no projeto.

- Os erros da API seguem o padrão `ProblemDetail`,
  permitindo respostas de erro padronizadas e mais fáceis de interpretar.

- A autenticação JWT utiliza os módulos `spring-boot-starter-oauth2-resource-server` e `spring-security-oauth2-jose` do Spring Security para geração,
  validação e processamento dos tokens.

- As alterações na estrutura do banco de dados são versionadas através do Liquibase, permitindo que o schema seja criado e atualizado junto com a aplicação.

- As API Keys são apresentadas em texto apenas no momento de sua criação. Para armazenamento, é persistido somente o hash da chave.

- O desenvolvimento do projeto contou com o auxílio de ferramentas de Inteligência Artificial como suporte durante a implementação, pesquisa e revisão de código.
