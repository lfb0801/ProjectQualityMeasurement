# Project Quality Management

## Documentation

When you run the command below, you will startup an instance of structurizr.
There you will find my documentation, ADR's and C4-diagrams.

```shell
docker compose --profile "docs" up -d
```

after you should be able to open your web-browser using this [link](http://localhost:9797) to view my documentation.

close using:

```shell
docker compose --profile "*" down -v
```

## Installation

Once you have set up SonarQube, you will receive a token (that shouldn't expire).
Paste that into your `.mvn/settings.xml` file, in the `<sonar.token>` tag
