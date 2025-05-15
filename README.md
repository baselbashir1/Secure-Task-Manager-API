# Please find the 'images' folder, which contains screenshots of the testing APIs.

# Secure Task Manager API 🔒

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1-green)
![Keycloak](https://img.shields.io/badge/Keycloak-21-orange)
![MySQL](https://img.shields.io/badge/MySQL-8.2-blue)
![Docker](https://img.shields.io/badge/Docker-✓-blue)

A secure and scalable Spring Boot API for task management, integrated with **Keycloak** for authentication and
role-based access control. Built with Docker for seamless deployment.

---

## Table of Contents

- [Features](#-features)
- [Technologies](#-technologies)
- [Prerequisites](#-prerequisites)
- [Quick Start](#-quick-start-with-docker)
- [Configuration](#-configuration)
    - [Keycloak Setup](#keycloak-setup)
    - [Database Configuration](#database-configuration)
- [API Documentation](#-api-documentation)
- [Security Model](#-security-model)
- [Logging & Monitoring](#-logging--monitoring)
- [Contributing](#-contributing)
- [License](#-license)

---

## 🚀 Features

- **🔐 AuthN/AuthZ**: JWT authentication via Keycloak with `role_admin` and `role_user` RBAC.
- **📝 Task Isolation**: Users can only access/modify their own tasks.
- **📊 Pagination & Sorting**: Efficient task retrieval with `page`, `size`, and `sort` params.
- **📈 Audit Logging**: Database-persisted request logs (IP, endpoint, status code).
- **🧩 Validation**: Robust input validation for tasks and user operations.
- **⚡ Error Handling**: Standardized JSON error responses for 400, 403, 404, etc.

---

## 🛠 Technologies

- **Backend**: Spring Boot 3.4.5, Java 17
- **Security**: Keycloak 25.0.5, Spring Security 6
- **Database**: MySQL 8.2 (Dockerized)
- **Logging**: AspectJ, Hibernate
- **Containerization**: Docker, Docker Compose
- **Build**: Maven

---

## 🐳 Quick Start with Docker

1. **Clone the repository**:
   ```bash
   git clone https://github.com/baselbashir1/Secure-Task-Manager-API.git

2. **Start Keycloak**:
   ```bash
   docker-compose up -d

3. **Access endpoints**:
    - API: `http://localhost:8080`
    - Keycloak Admin Console: `http://localhost:9090` (User: `admin`, Password: `password`)

---

### Keycloak Setup

1. **Configure Realm**:
    - Realm: `task-manager-realm`

2. **Configure Clients**:
    - Client ID: `task-manager`
    - Valid Redirect URIs: `http://localhost:8080/*`
    - Roles: `role_admin`, `role_user`

### Users created on Keycloak

| Username | Password | Role         |
|----------|----------|--------------|
| `admin`  | `admin1` | `role_admin` |
| `user`   | `user1`  | `role_user`  |

---

### API Documentation

#### Task Management Endpoints

| Endpoint             | Method   | Description                         | Parameters             | Required Role |
|----------------------|----------|-------------------------------------|------------------------|---------------|
| `/api/v1/tasks`      | `POST`   | Create a new task                   | -                      | `role_user`   |
| `/api/v1/tasks`      | `GET`    | Get paginated tasks belongs to user | `page`, `size`, `sort` | `role_user`   |
| `/api/v1/tasks/{id}` | `GET`    | Get task by id belongs to user      | -                      | `role_user`   |
| `/api/v1/tasks/{id}` | `PUT`    | Update task belongs to user         | -                      | `role_user`   |
| `/api/v1/tasks/{id}` | `DELETE` | Delete task belongs to user         | -                      | `role_user`   |

#### Admin Endpoints

| Endpoint                       | Method | Description                     | Required Role |
|--------------------------------|--------|---------------------------------|---------------|
| `/api/v1/keycloak/login`       | `POST` | Login to keycloak               | -             |
| `/api/v1/keycloak/users`       | `POST` | Create new user on keycloak     | `role_admin`  |
| `/api/v1/keycloak/roles`       | `POST` | Create new role on keycloak     | `role_admin`  |
| `/api/v1/adkeycloakmin/assign` | `POST` | Assign role to user on keycloak | `role_admin`  |

## 🔒 Security Implementation

### Authorization

- **Method-Level Security**: Endpoints are protected using Spring Security's `@PreAuthorize` annotations:
  ```java
  @PreAuthorize("hasRole('role_admin')")  // Admin-only endpoints
  @PreAuthorize("hasRole('role_user')")   // User-only endpoints
  @PreAuthorize("hasAnyRole('role_user', 'role_admin')")   // For both

- **SecurityLayerService**: Handles JWT claims extraction to retrieve authenticated user details. It can also be used as
  an alternative to @PreAuthorize, though a hybrid approach is implemented here.
    - `@PreAuthorize` Annotations: Provide a clear API contract for coarse-grained access control.
    - `SecurityLayerService`: Enables complex conditional logic for fine-grained business rules.
    - Combines the benefits of declarative (annotations) and imperative (service-layer) security.

---

## 🧪 Unit Testing

The repository includes comprehensive unit tests for the Task entity's CRUD operations using Spring Boot's `DataJpaTest`.

### Key Test Components
- **Test Framework**: JUnit 5 + Spring Boot Test
- **Test Focus**: Repository layer validation with H2 in-memory database
- **Assertions**: AssertJ for fluent assertions
- **Execution Order**: Explicit test ordering via `@Order` annotations
- **Transaction Handling**: Automatic rollback except for `@Rollback(false)` marked tests

### Test Coverage
| Test Method       | Description                                                                 |
|-------------------|-----------------------------------------------------------------------------|
| `saveTask()`      | Verifies task creation and auto-generated ID assignment                     |
| `getTask()`       | Validates single-task retrieval by ID                                       |
| `getAllTasks()`   | Ensures correct listing of all persisted tasks                              |
| `updateTask()`    | Tests field updates (e.g., title modification)                              |
| `deleteTask()`    | Confirms task deletion and post-deletion absence                            |

### Example Test Flow
1. **Create Task**: Persists a test task with owner ID "basel"
2. **Read Operations**: Validates retrieval via find-by-ID and find-all methods
3. **Update Task**: Modifies task title and confirms persistence
4. **Delete Task**: Removes entity and verifies cleanup

Tests can be executed via Maven:
```bash
mvn test