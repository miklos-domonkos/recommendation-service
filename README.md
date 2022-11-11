# Recommendation service

Imports crypto data from cvs files and provides different kind of recommendations.

Uses postgres database as datasource.

## Running the Application Locally

**A postgres database is required!**

Options

- install local database + run app
- run postgres with docker,
  example command

```bash
\recommendation-service\scripts> docker-compose --env-file .env -f .\docker-compose-postgres.yaml up
```

- build app docker image and run with docker-compose, example command

```bash
\recommendation-service\scripts> docker-compose --env-file .env -f .\docker-compose-postgres.yaml -f .\docker-compose.yaml up
```

- build app docker image and run with kubernetes, example command

```bash
\recommendation-service\scripts> kubectl apply -f k8s
```

_Note: May parameters be modified to run properly!_

Open a browser and visit [swagger-ui](http://localhost:8080/swagger-ui/index.html) for Swagger UI.

Open a browser and visit [api-docs](http://localhost:8080/v3/api-docs) for open api documentation.

Open a browser and visit [File import](http://localhost:8080) for file import ui.
