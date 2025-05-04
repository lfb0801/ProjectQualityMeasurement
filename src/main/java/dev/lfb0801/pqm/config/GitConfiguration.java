package dev.lfb0801.pqm.config;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Path;

@Configuration
public class GitConfiguration {

    @Value("quality.path")
    String path;

    @Bean
    public Repository repo() throws IOException {
        return new RepositoryBuilder()
                .setGitDir(Path.of(path).toFile())
                .build();
    }

    @Bean
    public Git git(Repository repo){
        return new Git(repo);
    }
}
