# LinkFlow — Link Management & Analytics Platform

LinkFlow is a Java Spring Boot application for creating, managing, and tracking shortened URLs, with user, employee, administrator, and developer-facing functionality.

## Current Features

- **Authentication & Security:** Registration, BCrypt password hashing, Spring Security, sessions, CSRF protection, and role-based authorization.
- **URL Shortening:** Unique short codes, link ownership, MySQL storage, and redirects.
- **Click Analytics:** Asynchronous click tracking, HMAC-SHA256 visitor fingerprinting, and Redis-based five-minute deduplication.
- **HTTPS:** TLS encryption for local development.
- **Docker:** Multi-stage builds, Docker Compose, MySQL, Redis, and persistent storage.
- **Email:** SMTP integration tested; email verification in development.

## Technology Stack

| Technology | Purpose |
|---|---|
| Java 23 | Backend |
| Spring Boot 4 | Application framework |
| Spring Security 7 | Security |
| Spring Data JPA / Hibernate | Persistence |
| MySQL 8 | Database |
| Redis 8 | Click deduplication |
| Maven | Build |
| Docker / Compose | Containerization |
| Spring Mail | Email |
| Postman | API testing |
| JavaScript | Planned frontend |

## Getting Started

### Requirements

- [Docker Desktop](https://www.docker.com/products/docker-desktop/)
- [Git](https://git-scm.com/)
- [Postman](https://www.postman.com/) (optional)
- Java 23 and Maven (for local development outside Docker)

### 1. Clone the repository

```bash
git clone https://github.com/HiandBye-1/LinkFlow.git
cd LinkFlow
```

### 2. Configure environment variables

Create a `.env` file in the project root using `.env.example` as a reference:

```dotenv
DB_USERNAME=[your database username]
DB_PASSWORD=[your database password]
KEYSTORE_PASSWORD=[your TLS certificate password]
DB_ROOT_PASSWORD=[your database root password]

SMTP_HOST=smtp.gmail.com
SMTP_PORT=587
SMTP_USERNAME=your-email@gmail.com
SMTP_PASSWORD=your-app-password

DB_HOST=localhost
HMAC_SECRET=generate using HmacSecretGenerator.java
```

Replace the placeholders with your own values. Never commit `.env` or real credentials.

### 3. Configure HTTPS

Create a certificate directory:

```bash
mkdir certs
```

Generate a self-signed PKCS12 certificate using Java's `keytool`:

```cmd
keytool -genkeypair ^
  -alias localhost ^
  -keyalg RSA ^
  -keysize 2048 ^
  -storetype PKCS12 ^
  -keystore certs/localhost.p12 ^
  -validity 365 ^
  -dname "CN=localhost" ^
  -ext "SAN=dns:localhost,ip:127.0.0.1"
```

When prompted, use the same password configured in `KEYSTORE_PASSWORD`.

The certificate is mounted into the application container, and Spring Boot uses it for HTTPS.

**Note:** This is a self-signed certificate intended for local development. Your browser or Postman may display a certificate warning.

### 4. Start LinkFlow

Make sure Docker Desktop is running:

```bash
docker compose up --build
```

Access the application through:

`https://localhost:8443`

MySQL initializes `DB.sql` when its data directory is empty. Existing database volumes persist between restarts.

## API Endpoints

| Method | Endpoint | Access | Purpose |
|---|---|---|---|
| POST | `/user/register` | Public | Register |
| POST | `/user/login` | Public | Log in |
| GET | `/csrf` | Public | Retrieve CSRF token |
| GET | `/user/profile` | Authenticated | View profile |
| POST | `/url/shorten` | NORMALUSER | Create short link |
| GET | `/r/{urlCode}` | Public | Redirect and track clicks |

CSRF tokens are required for protected POST requests.

## Architecture

LinkFlow follows a **layered architecture** within a modular monolith:

- **Controller:** HTTP requests and responses.
- **Service:** Business logic.
- **Repository:** Database operations.
- **Entity / DTO:** Data persistence and API structures.
- **Security:** Authentication and authorization.
- **Analytics:** Click tracking and event processing.

## Roadmap

- Complete email verification and MFA.
- Expand link management and analytics.
- Build user, employee/support, and administrator portals.
- Add developer APIs, API keys, and rate limiting.
- Integrate a JavaScript frontend.
- Add automated testing, monitoring, and cloud deployment.

---

**LinkFlow — Build, Manage, and Understand Your Links.**

TY for your interest in the project >:);