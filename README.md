# Purpose of the project
Project created to complete a master's degree course. It is based on microservice architecture. This application doesn't have complex functionality. It's purpose is to show basic cloud architecture.

# Tech stack
Each microservice is build with Java and Spring Framework and dockerized. RDBMS used in this projest is PostgreSQL 15.4, also dockerized. There is also API client which provides Thymeleaf template engine based GUI. All services communicate with each other using REST API.

# Architecture

## eureka-server
Microservice which is service discovery server. Clients are registering in it and then they are discoverable to each other by their names.

## oauth2-acs
Access controll service which is based on OAuth 2. It provides user identity from JPA repository. Passwords are hashed with 4-rounds Bcrypt. Also provides login page. It is possible to replace this microservice to one working as default OAuth 2 server implemented in organisation. 

## api-gateway
This microservice is gateway to the business logic microservices. This service contains routing definitions. It is implemented as as OAuth resource server.

## dict-service
One of business logic microservices. It provides dictionaries in-memory REST repository. This service is set up as Eureka discovery service client.

## crud-service
Second of business logic microservices. It is simple CRUD application. This microservice sends requests to the dict-service with Feign. This service is discoverable by Eureka server. 

## database
Database is initialized with script from db_schema directory. Whole directory is mapped as Docker volume and it is possibility to add more script files. In that case files are executed in their ascending by name. 

## simple-api-client
Only one not dockerized service. It provides GUI consuming api-gateway. This application is set up as OAuth 2 client. 

# Build & run
1. Project contains automatic PowerShell projects and Docker images build script named pipeline.ps1. To be executed correctly it needs installed Maven, Java 8, Java 17 and Docker. Run that script or create your own basing on provided.
2. Next step is modifying .env file with your own credentials or leave it with default data.
3. You can change basic user credentials modifying db_schema/db_schema.sql file. Just change inserts in to APP_USER and USER_AUTHORITY. When changing user credentials encrypt password with some online Bcrypt tool(4 rounds). Basic credentials: user/password
4. Add this line to your hosts file:
   ```
   127.0.0.1 oauth2-acs
   ```
5. Run
   ```
   docker compose up -d
   ```
   from your console (in older versions of Docker engine "docker compose" should be replaced with "docker-compose").
6. Build and run application simple-api-client
7. Default URL: http://127.0.0.1:8081/index
