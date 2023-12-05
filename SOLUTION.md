### Architecture

- Kotlin
- Spring Boot
- Maven
- Hexagonal architecture with modules:
    - `domain` - for business logic (central part)
    - `adapters` - for communication with DB (right part)
    - `app` - for controllers (left part) + app config
- Postgresql
- OpenApi
- Docker
- Testing
    - Frameworks: Junit5, Mockk, Kotest
    - Mother pattern to create random valid objects for tests
    - Unit testing for separate classes testing
    - Acceptance tests for testing the full integration starting from controllers

### How to use

1. Install `make` on your computer, if you do not already have it.
2. Start the application: `make up` and wait for the build and deployment
3. Visit the page: http://localhost:8080/swagger-ui/index.html to see the API description and try to iterate with the
   app

### TODO

- Add DTO validation. Valiktor for kotlin fits for that
- Handle more corner cases: e.q. a next openAt must be after previous closedAt
- Implement a client for communication with the service