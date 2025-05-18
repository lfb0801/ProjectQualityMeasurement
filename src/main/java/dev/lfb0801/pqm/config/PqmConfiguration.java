package dev.lfb0801.pqm.config;

import static java.util.Comparator.reverseOrder;

import static dev.lfb0801.pqm.Constants.TARGET_PATH;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "dev.lfb0801.pqm")
public class PqmConfiguration {

    private final String path;

    public PqmConfiguration(@Value("${quality.target.path}") String path) {
        this.path = path;
    }

    @Bean
    public Repository repo() throws IOException {
        Path foo = Path.of(TARGET_PATH.value);
        if (Files.exists(foo)) {
            Files.walk(foo)
                .sorted(reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
        }
        Files.createDirectory(foo);

        return new RepositoryBuilder().setWorkTree(new File(foo.toString()))
            .setup()
            .build();
    }

    @Bean
    public Git git(Repository repo) {
        Git git = new Git(repo);
        try {
            git.cloneRepository()
                .setURI(path)
                .setDirectory(repo.getWorkTree())
                .call();
        } catch (GitAPIException e) {
            throw new IllegalArgumentException("Repository should already exist", e);
        }
        return git;
    }
}
