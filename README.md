# Sports API

Sports API is a Spring Boot REST service for managing teams and players.

## What this project does

- Manage **teams** (create, list, get, update, patch, delete)
- Manage **players** (create/update, list, get, patch, delete)
- Support **player positions** (for example: Forward, Midfielder, Defender)
- Support **dynamic player searching** via query params on `/players`

## Tech stack

- Java
- Spring Boot
- Spring Data JPA
- Maven
- H2/PostgreSQL drivers

## Run locally

```bash
./mvnw spring-boot:run
```

## Run tests

```bash
./mvnw test
```

## API quick examples

### Teams

- `POST /teams`
- `GET /teams`
- `GET /teams/{id}`
- `PUT /teams/{id}`
- `PATCH /teams/{id}`
- `DELETE /teams/{id}`

### Players

- `PUT /players/{id}`
- `GET /players`
- `GET /players/{id}`
- `PATCH /players/{id}`
- `DELETE /players/{id}`

### Dynamic player search

Use optional query parameters on `GET /players`:

- `name`
- `position`
- `teamName`

Examples:

- `/players?position=def`
- `/players?name=yi&position=mid&teamName=tiger`

If no query params are provided, `/players` returns the standard paginated player list (backward compatible behavior).
