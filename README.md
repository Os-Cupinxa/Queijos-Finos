# Projeto Integrador 2024/07 - 2024/12
Objetivo:
O software "Sistema de Cadastro do PDI de Queijos Finos" tem como objetivo principal criar uma plataforma eficiente e intuitiva que permita o gerenciamento de propriedades envolvidas no Projeto de Queijos Finos. A plataforma será utilizada para cadastrar, atualizar e gerenciar informações essenciais sobre os produtores, propriedades, cursos e fornecedores, além de fornecer funcionalidades avançadas, como gráficos de desempenho e histórico de participação dos produtores no projeto.

### Tecnologias Utilizadas
- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- MySQL
- Swagger
- Maven 3.8.1
- Mockito
- JUnit
- Spring Test

### Instruções de Uso
#### _Instruções para execução do projeto (desenvolvimento):_
##### _Pré-requisitos:_
- Java 17
- Maven 3.8.1
- IDE de sua preferência
- MySQL


1. Clone o repositório
2. Importe o projeto na IDE de sua preferência
3. Execute o comando `mvn clean install -DskipTests` para baixar as dependências do projeto
4. Execute o projeto

#### _Instruções para execução do projeto (produção):_

##### _Pré-requisitos:_
- Docker
- Docker Compose

1. Clone o repositório
2. Execute o comando `docker-compose up` para subir o projeto
3. Acesse o projeto em `http://localhost:8080`

### Ferramentas Disponíveis para monitoramento, análise de código e desempenho
- SonarQube
- Grafana
- Prometheus
- OpenTelemetry
- Jaeger

## Documentação
#### _Documentação disponível em:_ "**./documentation"**
#### _Swagger api documentation:_ http://localhost:8080/swagger-ui.html


#### _Esquema do banco de dados:_
<p align="center">
  <img src="https://github.com/Os-Cupinxa/Queijos-Finos/blob/main/schema.png" alt="Schema" width="800">
</p>
