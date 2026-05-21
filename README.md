# MyLibrary v1.0

Sistema de Biblioteca Pessoal com backend em Spring Boot e frontend em Angular.

# NATANAEL SILVA DA CRUZ #

## Tecnologias

- Backend: Java 17, Spring Boot 3.x, Spring Web, Spring Data JPA, H2 Database e Bean Validation.
- Frontend: Angular 21, Reactive Forms e CSS simples.
- API base: `http://localhost:8080/api`

## Estrutura

```text
mylibrary/
  backend/
  frontend/
```

## Como rodar o backend

Entre na pasta do backend:

```bash
cd backend
mvn spring-boot:run
```

Se `mvn` nao estiver no PATH, rode o projeto pelo Maven da sua IDE ou adicione o Maven ao PATH.

O backend sobe em:

```text
http://localhost:8080
```

H2 Console:

```text
http://localhost:8080/h2-console
```

Dados de conexao do H2:

```text
JDBC URL: jdbc:h2:mem:mylibrary
User: sa
Password: deixe em branco
```

## Como rodar o frontend

Entre na pasta do frontend:

```bash
cd frontend
npm install
npm start
```

O frontend sobe em:

```text
http://localhost:4200
```

## Endpoints principais

Categorias:

- `GET /api/categorias`
- `GET /api/categorias/{id}`
- `POST /api/categorias`
- `DELETE /api/categorias/{id}`

Livros:

- `GET /api/livros`
- `GET /api/livros/{id}`
- `POST /api/livros`
- `DELETE /api/livros/{id}`
- `GET /api/livros?categoriaId=&status=&busca=`

Emprestimos:

- `GET /api/emprestimos`
- `GET /api/emprestimos/ativos`
- `GET /api/emprestimos/atrasados`
- `GET /api/livros/{id}/emprestimos`
- `POST /api/emprestimos/emprestar`
- `POST /api/emprestimos/{id}/devolver`

Dashboard:

- `GET /api/dashboard`

## Funcionalidades implementadas

- Cadastro, listagem e exclusao de categorias com nome unico.
- Bloqueio de exclusao de categoria quando ela possui livros.
- Cadastro, listagem, busca e filtros de livros por categoria, status e titulo/autor.
- Status inicial automatico `DISPONIVEL` para todo livro criado.
- Exclusao de livro somente quando estiver `DISPONIVEL`.
- Emprestimo de livro disponivel com pessoa, telefone e datas.
- Bloqueio de emprestimo para livro ja `EMPRESTADO`.
- Devolucao de emprestimo ativo com mudanca do livro para `DISPONIVEL`.
- Historico de emprestimos por livro.
- Dashboard com totais e lista dos 5 ultimos emprestimos.
- Relatorio de emprestimos atrasados com dias de atraso.
- Dados iniciais criados via `CommandLineRunner`.
- CORS configurado para `http://localhost:4200`.
