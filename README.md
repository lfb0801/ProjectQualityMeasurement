# Project Quality Management

## Documentation

When you run the command below, you will startup an instance of structurizr.
There you will find my documentation, ADR's and C4-diagrams.

```shell
docker-compose up -d
```

## Installation

Once you have setup SonarQube, you will receive a token (that shouldn't expire).
Paste that into your `.mvn/settings.xml` file, in the `<sonar.token>` tag
