# ManuTech

Sistema web para gerenciamento de ordens de manutenção em máquinas industriais.

O ManuTech permite cadastrar setores, técnicos, máquinas e ordens de serviço, facilitando o controle de manutenções, a organização dos técnicos responsáveis e o acompanhamento do status das ordens.

## Tecnologias utilizadas

### Backend
- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- PostgreSQL
- Maven
- Swagger/OpenAPI

### Frontend
- Vue 3
- TypeScript
- Vite
- Pinia
- Axios
- Vue Router

## Funcionalidades

- Login de usuários
- Controle de acesso por perfil
- Cadastro, listagem, edição e exclusão de máquinas
- Cadastro, listagem, edição e exclusão de técnicos
- Cadastro e acompanhamento de ordens de serviço
- Cadastro de setores
- Associação de técnicos a setores
- Associação de máquinas a setores
- Associação de ordens a máquinas e técnicos
- Classificação de ordens por prioridade
- Controle de status das ordens
- Integração entre frontend e backend

## Perfis de usuário

O sistema possui dois perfis principais:

### Gestor
Usuário com acesso às principais funcionalidades administrativas do sistema.

Pode acessar:
- Início
- Máquinas
- Técnicos
- Ordens de serviço

### Técnico
Usuário com acesso voltado ao acompanhamento das ordens de manutenção.

Pode acessar:
- Início
- Ordens de serviço

## Usuários para teste

Ao iniciar o backend, o sistema cadastra automaticamente alguns dados para teste e apresentação.

| Usuário | Senha | Perfil |
|---|---|---|
| gestor | 123456 | GESTOR |
| tecnico | 123456 | TECNICO |
| ana | 123456 | TECNICO |

## Estrutura do projeto

```text
Projeto-Manutech-git/
├── backend/
│   └── ManuTech/
│       ├── src/
│       ├── pom.xml
│       └── README.md
│
├── manutech/
│   ├── src/
│   ├── package.json
│   └── vite.config.ts
│
├── docs/
│   └── documentos do projeto
│
└── README.md
