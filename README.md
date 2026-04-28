#Registration Service 

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
### 🧩 Start project
For starting the project be sure that you have Docker and Docker Desktop on your machine.

``` bash
git clone https://github.com/pepegazxc/RegistrationService-appealsProject.git
cd RegistrationService-appealsService
```

Create in the project .env file with:

POSTGRES_PASSWORD=password  
POSTGRES_USERNAME=user  
POSTGRES_DB=postgres  
REDIS_PASSWORD=password  
REDIS_PORT=6379  
REDIS_HOST=localhost  
JASYPT_SALT=salt  
JASYPT_ALGORITHM=algorithm  
JASYPT_PASSWORD=password  
JWT_KEY=key  
MAIL_PASSWORD=password
MAIL_USERNAME=email
MAIL_HOST=host
MAIN_MAIL=email
APP_URL=http://localhost:8081

And then:
```
docker-compose up -d
```
