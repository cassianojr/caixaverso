# API de Produtos e Simulação de Empréstimos – CAIXAVERSO

## Sobre

Essa API permite cadastrar, atualizar, consultar e remover produtos de crédito, além de simular empréstimos com cálculo de parcelas e validações de negócio.

## Arquitetura

O projeto segue **Arquitetura Hexagonal (Ports & Adapters)**:

* `domain` → modelos e regras de negócio (`Produto`, `Simulacao`, `ResultadoSimulacao`, exceções).
* `application` → casos de uso (`*UseCase`) e serviços de simulação.
* `ports` → interfaces que definem contratos:

  * `inbound` → entrada (ex: `SimulacaoPort`)
  * `outbound` → saída (ex: `ProdutoRepository`)
* `adapters` → implementações concretas:

  * `inbound/rest` → recursos REST (JAX-RS)
  * `outbound/persistence` → persistência com Panache/JPA

## Estrutura de Pastas

```
src/main/java/br/gov/caixa
 ├─ domain
 │   ├─ model
 │   └─ exception
 ├─ application
 │   ├─ usecase
 │   └─ service
 ├─ ports
 │   ├─ inbound
 │   └─ outbound
 └─ adapters
     ├─ inbound/rest
     └─ outbound/persistence
```

## Tecnologias

* Java 17
* Quarkus 3 (RESTEasy Reactive, Jackson, CDI, Hibernate ORM + Panache)
* Banco de dados em memória: H2 (dev/testes)
* Testes: JUnit 5, Mockito, Rest-Assured
* Observabilidade: Health (`smallrye-health`), OpenAPI (`smallrye-openapi`)
* Qualidade: Jacoco, SonarCloud

## Rodando a aplicação

**Requisitos:** Java 17 e Maven

Modo dev com hot reload:

```bash
./mvnw quarkus:dev   # Linux/Mac
./mvnw.cmd quarkus:dev  # Windows
```

Acessos úteis:

* Swagger / OpenAPI: [http://localhost:8080/q/swagger-ui](http://localhost:8080/q/swagger-ui)
* Health: [http://localhost:8080/q/health](http://localhost:8080/q/health)

## Testes

Para rodar os testes e gerar relatório de cobertura Jacoco:

```bash
./mvnw test
```

Relatórios gerados em:

```
target/site/jacoco/index.html
target/jacoco-xml/jacoco.xml  # para Sonar
```

## Endpoints principais

| Método | Caminho                 | Descrição                          |
| ------ | ----------------------- | ---------------------------------- |
| GET    | /produtos/{id}          | Buscar produto por ID              |
| GET    | /produtos               | Listar todos os produtos           |
| POST   | /produtos               | Criar novo produto                 |
| PUT    | /produtos/{id}          | Atualizar produto                  |
| DELETE | /produtos/{id}          | Remover produto                    |
| POST   | /simulacoes/{idProduto} | Simular empréstimo para um produto |

### Exemplo: Criar Produto

```json
POST /produtos
{
  "nome": "Crédito Consignado",
  "taxaJurosAnual": 18.5,
  "prazoMaximoMeses": 60
}
```

### Exemplo: Simular Empréstimo

```json
POST /simulacoes/1
{
  "valorSolicitado": 10000.00,
  "prazoMeses": 24
}
```

## Casos de Uso

* Criar, atualizar e remover produto
* Listar e buscar produto
* Simular empréstimo (valida prazo <= prazo máximo, calcula parcelas)

## Docker

Gerar artefato:

```bash
./mvnw package
```

Build da imagem (exemplo com Dockerfile.jvm):

```bash
docker build -f src/main/docker/Dockerfile.jvm -t cassianojr-api .
```

Rodar o container:

```bash
docker run -p 8080:8080 -e DB_PASSWORD=senha cassianojr-api
```