## Implementation

### Lines of Code

I am able to make detailed scans over the content of source files.
Thanks to the [LOCC4J](https://github.com/cthing/locc4j) library.
This allows me to ignore blank and comments in my analysis.

### JGit

Thanks to [JGit](https://github.com/eclipse-jgit/jgit) I am able to interact with the VCS of the given project and
filter/focus on specific parts.

It also allows me to increase the interval of analysis.
This is important as running my analysis on every build/commit would be expensive and wouldn't align with my research.
