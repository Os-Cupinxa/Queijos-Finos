
# Documentação do MobileDTOsController

## Visão Geral

O `MobileDTOsController` é um controlador Spring MVC que fornece endpoints para o aplicativo móvel acessar diversos objetos de transferência de dados (DTOs). Esses endpoints incluem insights para dashboards, agendas futuras, contratos prestes a vencer, detalhes de propriedades e estatísticas de produção de leite ao longo do tempo.

---

## Índice

- [Endpoints](#endpoints)
  - [GET /dataInsight](#get-datainsight)
  - [GET /agendaAndExpiringContracts](#get-agendaandexpiringcontracts)
  - [GET /propriedadesDTO](#get-propriedadesdto)
  - [GET /propriedadesDTO/producerName](#get-propriedadesdtoproducername)
  - [GET /dataPointYear](#get-datapointyear)
  - [GET /dataPoint](#get-datapoint)
- [Métodos Auxiliares](#métodos-auxiliares)
  - [getQuantitiesForYear](#getquantitiesforyear)
  - [getQuantitiesForWeek](#getquantitiesforweek)
  - [getTimeLabels](#gettimelabels)
  - [getAbbreviatedMonthLabels](#getabbreviatedmonthlabels)
- [Dependências](#dependências)
- [Notas](#notas)

---

## Endpoints

### GET /dataInsight

**Descrição:**

Obtém insights de dados para o dashboard, incluindo contagens de propriedades por status.

**URL:** `/dataInsight`

**Método HTTP:** `GET`

**Resposta:**

- Retorna um objeto `DataInsightDTO` contendo:
  - `active`: Número de propriedades com status `2`.
  - `activeInContemplation`: Número de propriedades com status `1`.
  - `dropout`: Número de propriedades com status `0`.

**Exemplo:**

```json
{
  "active": 150,
  "activeInContemplation": 75,
  "dropout": 25
}
```

**Detalhes da Implementação:**

- As contagens são obtidas usando `propriedadeRepo.countBystatus(int status)` para cada valor de status.
- As contagens são convertidas de `long` para `Integer`.

---

### GET /agendaAndExpiringContracts

**Descrição:**

Busca agendas futuras para os próximos 7 dias e contratos que vencem nos próximos 30 dias para o usuário autenticado.

**URL:** `/agendaAndExpiringContracts`

**Método HTTP:** `GET`

**Cabeçalhos:**

- `Authorization`: Token JWT no formato Bearer (por exemplo, `Bearer seu_token_jwt`).

**Resposta:**

- Retorna uma lista `List<AgendaItemsDTO>` contendo:
  - `nome`: Nome associado ao item (usuário ou propriedade).
  - `descricao`: Descrição do item da agenda ou contrato.
  - `data`: Data do evento.
  - `tipo`: Tipo do item (`"Visita"` ou `"Contrato"`).

**Exemplo:**

```json
[
  {
    "nome": "Antony Bresolin",
    "descricao": "Visita técnica",
    "data": "2023-10-15",
    "tipo": "Visita"
  },
  {
    "nome": "Fazenda Teste",
    "descricao": "Contrato de Arrendamento",
    "data": "2023-11-01",
    "tipo": "Contrato"
  }
]
```

**Detalhes da Implementação:**

- Extrai o `userId` do token JWT usando `jwtUtils.extractClaims(token)`.
- Busca agendas e contratos usando métodos dos repositórios:
  - `agendaRepository.findFuturesAgendasByUserId(userId, currentDateSQL, futureDateAgendaSQL)`
  - `contratoRepository.findExpiringContracts(currentDateSQL, futureDateContractsSQL)`
- Mapeia os resultados para `AgendaItemsDTO`.
- Combina agendas e contratos em uma única lista.

---

### GET /propriedadesDTO

**Descrição:**

Obtém uma lista paginada de detalhes das propriedades.

**URL:** `/propriedadesDTO`

**Método HTTP:** `GET`

**Parâmetros:**

- `page` (opcional): Número da página (padrão é `0`).
- `size` (opcional): Número de itens por página (padrão é `20`).

**Resposta:**

- Retorna um `Page<PropriedadeDTO>` contendo os detalhes das propriedades.

**Exemplo:**

```json
{
  "content": [
    {
      "name": "Fazenda do Antony",
      "owner": "Antony Bresolin",
      "city": "Curitiba",
      "state": "PR",
      "status": 1,
      "latitude": "-25.4284",
      "longitude": "-49.2733",
      "contratos": [],
      "tecnologias": []
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20
  },
  "totalPages": 5,
  "totalElements": 100
}
```

---

### GET /dataPointYear

**Descrição:**

Fornece dados de produção de leite por mês para o ano atual e anterior.

**URL:** `/dataPointYear`

**Método HTTP:** `GET`

**Resposta:**

- Retorna um objeto `DataPointDTOYear` contendo dados de produção.

**Exemplo:**

```json
{
  "curYear": [1000.0, 1200.0, 1100.0],
  "pastYear": [900.0, 1100.0, 1050.0],
  "time": ["JAN", "FEV", "MAR"]
}
```

---

## Dependências

- **Repositórios:**
  - `PropriedadeRepository`
  - `AmostraRepository`
  - `ContratoRepository`
  - `AgendaRepository`

- **Utilitários:**
  - `JwtUtils` para manipulação de tokens JWT.

- **Modelos e DTOs:**
  - Modelos: `Propriedade`, `Amostra`, `Contrato`, `Agenda`
  - DTOs: `DataInsightDTO`, `AgendaItemsDTO`, `PropriedadeDTO`, `DataPointDTO`, `DataPointDTOYear`

---

## Notas

- **Autenticação:** A maioria dos endpoints requer um token JWT válido.
- **Paginação:** Os endpoints `/propriedadesDTO` e `/propriedadesDTO/producerName` suportam paginação.
