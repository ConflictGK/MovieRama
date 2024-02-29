# MovieRama

A social sharing platform where users can share their favorite movies.

## Quick Start Guide for Java Spring Boot Application

### Prerequisites

- **Docker**: Ensure Docker and Docker Compose are installed.
- **Maven**: Required for building the project.

### Build and Run

#### Build the Project

Execute the following Maven command to build your project:

```bash
mvn clean install
```

### Start Services

Use Docker Compose to start the application and its services:

```bash
docker-compose up --build
```

### Accessing the Application

- **Web Application**: Accessible at http://localhost:8080/movies
- **Database**: PostgreSQL available at localhost:5432 with the environment credentials provided in **docker-compose.yml**.

### Shutdown

Stop all containers running:

```bash
docker-compose down
```
