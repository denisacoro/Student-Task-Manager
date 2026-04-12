# Student Task Manager

Student Task Manager is a Spring Boot REST API built to practice authentication, authorization, secure endpoint design, and testing in a clean and structured way.

The application allows users to register, authenticate, create personal tasks, and view only their own tasks. Admin users have additional permissions to view all tasks, delete any task, and create new users.

## Features

- User registration and login
- Role-based authorization with `ROLE_USER` and `ROLE_ADMIN`
- Secure REST API endpoints with Spring Security
- Password hashing with BCrypt
- Task creation and retrieval for authenticated users
- Admin-only management endpoints
- Unit tests with JUnit and Mockito
- Integration tests with Spring Boot test support

## Tech Stack

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- PostgreSQL
- Maven
- JUnit 5
- Mockito

## Project Structure

```text
src
├── main
│   ├── java/com/example/studenttaskmanager
│   │   ├── config
│   │   ├── controller
│   │   ├── dto
│   │   ├── entity
│   │   ├── exception
│   │   ├── repository
│   │   ├── security
│   │   ├── service
│   │   └── StudentTaskManagerApplication.java
│   └── resources
│       └── application.properties
└── test
    ├── java/com/example/studenttaskmanager
    │   ├── service
    │   ├── AuthIntegrationTest.java
    │   └── StudentTaskManagerApplicationTests.java
    └── resources
        └── application-test.properties
```

## Roles and Permissions

### ROLE_USER
- Can create a new task
- Can view only their own tasks

### ROLE_ADMIN
- Can view all tasks
- Can delete any task
- Can create new users

## API Endpoints

### Public Endpoints

#### Register a new user
`POST /auth/register`

Example request body:

```json
{
  "username": "denisa",
  "password": "password123"
}
```

#### Login
`POST /auth/login`

Example request body:

```json
{
  "username": "denisa",
  "password": "password123"
}
```

### User / Admin Endpoints

#### Get tasks for the logged-in user
`GET /tasks/my`

#### Create a new task
`POST /tasks`

Example request body:

```json
{
  "title": "Finish project",
  "description": "Implement security and tests"
}
```

### Admin Endpoints

#### Get all tasks
`GET /tasks`

#### Delete a task
`DELETE /tasks/{id}`

#### Create a new user
`POST /admin/users`

Example request body:

```json
{
  "username": "admin2",
  "password": "adminpass",
  "role": "ROLE_ADMIN"
}
```

## Security

This project uses Spring Security with:

- Username/password authentication
- BCrypt password encoding
- Role-based access control
- Public and protected routes

Protected endpoints are accessed using HTTP Basic authentication in Postman or another API client.

## Database Configuration

The application uses PostgreSQL as the main database.

Example `application.properties` configuration:

```properties
spring.application.name=student-task-manager
server.port=8081

spring.datasource.url=jdbc:postgresql://localhost:5432/student_task_manager
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

## How to Run the Project

### 1. Clone the repository

```bash
git clone <your-repository-url>
cd student-task-manager
```

### 2. Create the PostgreSQL database

```sql
CREATE DATABASE student_task_manager;
```

### 3. Configure database credentials

Update `src/main/resources/application.properties` with your PostgreSQL username and password.

### 4. Run the application

Using IntelliJ:
- Open the project
- Wait for Maven dependencies to load
- Run `StudentTaskManagerApplication`

Or using Maven Wrapper:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

## Running Tests

Run all tests with Maven Wrapper:

```bash
./mvnw test
```

On Windows:

```bash
mvnw.cmd test
```

## Testing Coverage

### Unit Tests
- `UserServiceTest`
- `TaskServiceTest`

### Integration Tests
- Access protected endpoint without authentication
- Access admin endpoint with a normal user
- Register a new user successfully

## Example Authentication in Postman

For protected endpoints such as:

- `POST /tasks`
- `GET /tasks/my`
- `GET /tasks`
- `DELETE /tasks/{id}`
- `POST /admin/users`

use the **Authorization** tab in Postman and select:

- **Type:** Basic Auth
- **Username:** your username
- **Password:** your password

## Future Improvements

- JWT-based authentication
- Better error response structure
- Swagger / OpenAPI documentation
- Dockerized PostgreSQL setup
- More advanced task management features

## Author

Developed as a Spring Boot practice project focused on authentication, authorization, and testing.
