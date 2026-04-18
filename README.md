# 🎬 MovieZ - Plataforma de Reviews de Filmes

O **MovieZ** é uma aplicação web desenvolvida para entusiastas do cinema que desejam descobrir novos filmes, compartilhar suas opiniões através de reviews e interagir com outros usuários seguindo seus perfis.

Este projeto foi desenvolvido como parte de um trabalho acadêmico da matéria Desenvolvimento de Software II, utilizando tecnologias modernas de desenvolvimento Java.

---

## Funcionalidades Principais

- **Autenticação Segura**: Registro de usuários, login e controle de acesso com Spring Security.
- **Busca de Filmes**: Integração com a API do TMDB para pesquisar filmes.
- **Sistema de Reviews**: Os usuários podem escrever, editar e excluir avaliações de filmes.
- **Rede Social**: Sistema de "Seguir" outros usuários para acompanhar suas atividades.
- **Perfil do Usuário**: Página dedicada com informações do usuário e histórico de avaliações.
- **Dashboard Administrativo**: Inicialização automática de um usuário administrador para gestão do sistema.

---

## 🛠️ Tecnologias Utilizadas

A aplicação foi construída utilizando o ecossistema Spring:

- **Linguagem**: Java 17
- **Framework Web**: [Spring Boot 3.5.7](https://spring.io/projects/spring-boot)
- **Segurança**: Spring Security (BCrypt para criptografia de senhas)
- **Persistência de Dados**: Spring Data JPA / Hibernate
- **Banco de Dados**: PostgreSQL
- **Template Engine**: Thymeleaf (HTML dinâmico)
- **Consumo de API**: RestTemplate para comunicação com o TMDB
- **Utilitários**: Lombok (Redução de boilerplate) e Spring Validation
- **Containerização**: Docker & Docker Compose

---

## ⚙️ Configuração e Execução

### Pré-requisitos
- JDK 17 ou superior
- Maven 3.8+
- PostgreSQL rodando localmente (ou via Docker)
- Uma chave de API do [The Movie Database (TMDB)](https://www.themoviedb.org/documentation/api)

### Variáveis de Ambiente
A aplicação espera as seguintes variáveis de ambiente configuradas no `application.properties` ou no sistema:

| Variável | Descrição |
|----------|-----------|
| `PORT` | Porta onde a aplicação irá rodar (Ex: 8080) |
| `DATABASE_URL` | URL de conexão JDBC (Ex: `jdbc:postgresql://localhost:5432/moviez`) |
| `DB_USERNAME` | Usuário do banco de dados |
| `DB_PASSWORD` | Senha do banco de dados |
| `TMDB_API_KEY` | Sua API Key do TMDB |
| `ADMIN_EMAIL` | Email para o usuário administrador inicial |
| `ADMIN_PASSWORD` | Senha para o usuário administrador inicial |
| `ADMIN_USERNAME` | Username para o usuário administrador inicial |

---

## 📂 Estrutura do Projeto

- `src/main/java/com/moviez/DSII_P2/config`: Configurações de segurança, API externa e inicialização de dados.
- `src/main/java/com/moviez/DSII_P2/controller`: Controladores Web (MVC).
- `src/main/java/com/moviez/DSII_P2/model`: Entidades JPA e DTOs.
- `src/main/java/com/moviez/DSII_P2/repository`: Interfaces de acesso ao banco de dados.
- `src/main/java/com/moviez/DSII_P2/service`: Lógica de negócio da aplicação.
- `src/main/resources/templates`: Páginas HTML (Thymeleaf).

---

## Equipe

- [**Gabriella de Sousa**](https://github.com/gabrielladsousa)
- **Héber Neves**
- [**Luiza Menezes**](https://github.com/luizamenezesg)

---
