### ✍️ About the service
**Registration Service** - is a part of the Appeals Project.  
It handles user registration, authentication, and JWT token generation used across other services.

---
### ⚒️ Tech
- Java 21
- Spring Boot 4.0.0
- Spring Security
- Redis
- Postgres
- JWT
- Lombok
- Argon2
- Jasypt
- FlyWay
- Maven
- JUnit
- Mockito
- Apache Kafka (KRaft)

---
### 🖋️ Project functional

1. *Register new users*
2. *Authenticate existing users*
3. *Refresh JWT tokens*  
4. *Handle role-based requests (admin / mayor level)*
---

### 🗄️ DataBase diagram

![imageOfDB](./forReadme/DB_UML_image.png)
---
### ⚙️ Project Structure
``` bash
├── Dockerfile
├── docker-compose.yaml
├── forReadMe
├── pom.xml
├── src
│   ├── main
│   │   ├── java
│   │   │   └── main
│   │   │       ├── advice
│   │   │       │   ├── factory
│   │   │       │   └── mapper
│   │   │       ├── configuration
│   │   │       │   └── auth
│   │   │       ├── controller
│   │   │       ├── dto
│   │   │       │   ├── enums
│   │   │       │   ├── request
│   │   │       │   └── response
│   │   │       ├── entity
│   │   │       ├── event
│   │   │       ├── exception
│   │   │       │   ├── cryptography
│   │   │       │   ├── email
│   │   │       │   ├── request
│   │   │       │   ├── security
│   │   │       │   └── user
│   │   │       ├── producer
│   │   │       ├── repository
│   │   │       ├── schedule
│   │   │       ├── security
│   │   │       └── service
│   │   │           ├── application
│   │   │           ├── infrastructure
│   │   │           └── support
│   │   └── resources
│   └── test
```

---
### 🧩 Start project
For starting the project be sure that you have Docker and Docker Desktop on your machine.

(Optional) The first step is clone the project from DockerHub:
``` bash
docker pull pepegazxc/registration-service:1.0
```

OR you can clone it from GitHub:
``` bash
git clone https://github.com/pepegazxc/RegistrationService-appealsProject.git
cd RegistrationService-appealsProject
```

Then your must create .env file:
``` bash
touch .env
```

And then fill it (example data):
``` file
POSTGRES_PASSWORD=password
POSTGRES_USERNAME=user
POSTGRES_DB=postgres
POSTGRES_URL=jdbc:postgresql://localhost:5432/postgres
REDIS_PASSWORD=password
REDIS_PORT=6379
REDIS_HOST=localhost
JASYPT_SALT=salt
JASYPT_ALGORITHM=algo
JASYPT_PASSWORD=password
JWT_KEY=jwt_key
INTERNAL_SECRET=internal_secret
```

And then run the containers:
``` bash
docker-compose up -d
```

---
### 🔙 Back to navigate repository

Navigate repository: [AppealsProject](https://github.com/pepegazxc/Appeals-Project.git)
