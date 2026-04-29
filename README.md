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
- Java Mail
- Lombok
- Argon2
- Jasypt
- FlyWay
- Maven
- JUnit
- Mockito

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
├── README.md
├── docker-compose.yaml
├── mvnw
├── mvnw.cmd
├── pom.xml
├── src
│   ├── main
│   │   ├── java
│   │   │   └── main
│   │   │       ├── RegistrationServiceAppealsProjectApplication.java
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
│   │   │       │   ├── email
│   │   │       │   ├── request
│   │   │       │   ├── security
│   │   │       │   └── user
│   │   │       ├── listener
│   │   │       ├── repository
│   │   │       ├── schedule
│   │   │       ├── security
│   │   │       └── service
│   │   │           ├── application
│   │   │           ├── infrastructure
│   │   │           │   ├── jwt
│   │   │           │   └── mail
│   │   │           └── support
│   │   └── resources
│   │       ├── application.yml
│   │       └── migration.db
│   └── test
│       └── java

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
POSTGRES_URL=jdbc:postgresql://postgres:5432/postgres
REDIS_PASSWORD=password  
REDIS_PORT=6379  
REDIS_HOST=redis  
JASYPT_SALT=salt  
JASYPT_ALGORITHM=algorithm  
JASYPT_PASSWORD=password  
JWT_KEY=key  
MAIL_PASSWORD=password 
MAIL_USERNAME=email
MAIL_HOST=host 
MAIN_MAIL=email
APP_URL=http://localhost:8081
```

And then run the containers:
``` bash
docker-compose up -d
```

---
### 🔙 Back to navigate repository

Navigate repository: [AppealsProject](https://github.com/pepegazxc/Appeals-Project.git)