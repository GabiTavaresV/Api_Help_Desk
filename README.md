# Simulador de Help Desk

## Descrição do Projeto
O Simulador de Help Desk é uma aplicação desenvolvida para gerenciar o atendimento a chamados de usuários, permitindo o registro, acompanhamento e resolução de solicitações.

## Funcionalidades

### 1. Cadastro de Usuário
- Permite o cadastro de usuários do sistema:
    - **name** nome do usuário
    - **email** email do usuário

### 2. Cadastro do Atendente
- Permite o cadastro do atendente:
    - **name** nome do atendente

### 3. Cadastro do Equipamento
- Permite o cadastro do atendente:
    - **serialNumber** número de série do equipamento

### 4. Cadastro de Balcões de Atendimento
- Permite o cadastro de balcões de atendimento com as seguintes informações:
    - ### name Nome do atendente do balcão

### 3. Cadastro de Chamados
- Os chamados incluem as seguintes informações:
    - **usuário (customerId)**: Identificação do cliente que abriu o chamado.
    - **balcão**: Balcão de atendimento que será atribuido o chamado.
    - **equipamento**: Equipamento referente ao chamado.
    - **Motivo do Chamado**: Descrição do motivo pelo qual o chamado foi aberto.
    - **atendente**: Atendente que irá prosseguir com o chamado.

### 4. Listar Usuários
- Listar todos os usuários.
- Listar usuário por `Id`.

### 5. Listar Atendente
- Listar todos os atendentes.
- Listar atendente por `Id`.

### 6. Listar Equipamento
- Listar todos os equipamentos.
- Listar equipamento por `Id`.

### 7. Listar Balcão
- Listar todos os balcões.
- Listar balcão por `Id`.

### 4. Listar Chamados
- Listar todos os chamados com paginação.
- Listar todos os chamados por `customerId` com paginação.

### 5. Detalhes do Chamado
- Exibir detalhes de um chamado específico, incluindo:
    - Motivo do Chamado
    - Equipamento do Chamado
    - Cliente que abriu o chamado
    - Atendente atribuído ao chamado
    - Data do Chamado
    - Data de Resolução
    - Status do Chamado

## Tecnologias Utilizadas
    - Linguagem de programação: Java
    - Framework: Spring Boot
    - Banco de Dados: PostgreSQL

