## Implementation

In this section I will explain the building-blocks of my implementation.

### Lines of Code

I am able to make detailed scans over the content of source files.
Thanks to the [LOCC4J](https://github.com/cthing/locc4j) library.
This allows me to ignore blank and comments in my analysis.

### JGit

Thanks to [JGit](https://github.com/eclipse-jgit/jgit?tab=readme-ov-file#java-git) I am able to interact with the VCS of
the given project and
filter/focus on specific parts.

It also allows me to increase the interval of analysis.
This is important as running my analysis on every build/commit would be expensive and wouldn't align with my research.

### SonarQube

Using a local version of SonarQube:

````shell
mvn clean verify sonar:sonar \
  -Dsonar.projectKey=Project-Quality-Measurement \
  -Dsonar.projectName='Project Quality Measurement' \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.token=sqp_6a0467fd7a672b9c15428c1be97e07d271079388
````
