# LinkFlow — Link Management & Analytics Platform

LinkFlow is a Java Spring Boot application for creating, managing, and tracking shortened URLs, with user, employee, administrator, and developer-facing functionality.

## Current Features

- **Authentication:** Registration, BCrypt password hashing, Spring Security, sessions, and CSRF protection.
- **Authorization:** Role-based access control.
- **URL Shortening:** Unique short codes, link ownership, MySQL storage, and redirects.
- **HTTPS:** TLS support for local development.
- **Docker:** Multi-stage builds, Docker Compose, MySQL, and persistent storage.
- **Email:** SMTP integration tested; email verification is under development.

## Technology Stack

| Technology | Purpose |
|---|---|
| Java 23 | Backend |
| Spring Boot 4 | Application framework |
| Spring Security 7 | Security |
| Spring Data JPA / Hibernate | Persistence |
| MySQL 8 | Database |
| Maven | Build |
| Docker / Compose | Containerization |
| Spring Mail | Email delivery |
| Postman | API testing |
| JavaScript | Planned frontend |

## Getting Started

### Requirements

- [Docker Desktop](https://www.docker.com/products/docker-desktop/)
- [Git](https://git-scm.com/)
- [Postman](https://www.postman.com/) (optional)

### 1. Clone the repository

```bash
git clone https://github.com/HiandBye-1/LinkFlow.git
cd LinkFlow
```

### 2. Configure environment variables

Create a `.env` file in the project root:

```dotenv
DB_USERNAME=appuser
DB_PASSWORD=change_me
DB_ROOT_PASSWORD=change_me
KEYSTORE_PASSWORD=change_me

SMTP_HOST=smtp.gmail.com
SMTP_PORT=587
SMTP_USERNAME=your-email@gmail.com
SMTP_PASSWORD=your-app-password
```

Replace the example values with your own. Never commit `.env` or real credentials.

### 3. Configure Gmail SMTP (optional)

To enable application emails:

1. Enable Google two-step verification.
2. Generate a [Google App Password](https://myaccount.google.com/apppasswords).
3. Set your Gmail address as `SMTP_USERNAME`.
4. Set the generated App Password as `SMTP_PASSWORD`.

LinkFlow uses this SMTP account to send emails to users. Users do not need their own SMTP configuration.

### 4. Create the HTTPS certificate

Create the certificate directory:

```bash
mkdir certs
```

Generate a local development certificate:

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

Use the same password as `KEYSTORE_PASSWORD`. The self-signed certificate may trigger browser warnings during local development.

### 5. Start LinkFlow

Make sure Docker Desktop is running:

```bash
docker compose up --build
```

Application: `https://localhost:8443`

MySQL initializes `DB.sql` when its data directory is empty. Existing database volumes are preserved between restarts.

## API Usage

CSRF protection applies to POST requests. First retrieve a token:

```http
GET /csrf
```

Keep the session cookie and send the returned token in the `X-CSRF-TOKEN` header for POST requests.

| Method | Endpoint | Access | Purpose |
|---|---|---|---|
| POST | `/user/register` | Public | Register |
| POST | `/user/login` | Public | Log in |
| GET | `/csrf` | Public | Retrieve CSRF token |
| GET | `/user/profile` | Authenticated | View profile |
| POST | `/url/shorten` | NORMALUSER | Create short link |
| GET | `/r/{urlCode}` | Public | Redirect |

### Register

`POST /user/register`

```json
{
  "username": "exampleuser",
  "password": "yourpassword",
  "reenteredPassword": "yourpassword"
}
```

### Create a short link

`POST /url/shorten`

```json
{
  "longUrl": "https://example.com"
}
```

### Email verification (in development)

The planned workflow:

1. Authenticated user submits an email address.
2. LinkFlow generates a six-digit code.
3. The code is hashed and temporarily stored in MySQL.
4. SMTP sends the code to the submitted address.
5. The user submits the code for verification.
6. On success, the email is saved to the account.

Verification codes expire after five minutes.

## Useful Docker Commands

| Command | Purpose |
|---|---|
| `docker compose up` | Start containers |
| `docker compose up --build` | Build and start |
| `docker compose down` | Stop and remove containers |
| `docker compose logs app` | View application logs |
| `docker compose logs database` | View database logs |
| `docker compose ps` | Check container status |

**Warning:** `docker compose down -v` deletes named volumes, including stored database data.

## Project Architecture

LinkFlow uses a layered architecture:

- **Controller:** HTTP requests and responses.
- **Service:** Business logic.
- **Repository:** Database operations.
- **Entity:** Database records.
- **DTO:** API request and response structures.
- **Security:** Authentication and authorization.
- **Exception:** Centralized error handling.

The intended design is a **modular monolith**.

## Roadmap

- Complete email verification and add MFA.
- Link management, campaigns, and click analytics.
- User, employee/support, and administrator portals.
- Developer APIs, API keys, and rate limiting.
- Asynchronous analytics processing.
- JavaScript frontend integration.
- Automated testing, monitoring, and cloud deployment.

## Development Approach

The project prioritizes backend functionality, security, correctness, maintainability, and testing. New technologies will be introduced when they solve practical engineering problems.

---

**LinkFlow — Build, Manage, and Understand Your Links.**

TY for your interest in the project >:);