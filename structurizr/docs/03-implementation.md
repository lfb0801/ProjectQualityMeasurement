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

In this project I use a local version of SonarQube, but normally I would expect a company to have already setup their
own SonarQube instance somewhere.

The reason I'm using SonarQube, is to make it do the Static Code Analysis.
This is only a cherry on the cake for my project, as it is mostly concerned the [JGit](#JGit) analysis

#### SonarQube also does LOC

The main reason why we don't use the LOC from SonarQube, is because SonarQube only looks at programming languages.
Therefor relying on SonarQube for LOC, would not be able to detect a problem caused by config files.
