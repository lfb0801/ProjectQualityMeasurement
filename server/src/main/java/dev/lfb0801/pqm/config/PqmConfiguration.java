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
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(PMAProperties.class)
@ComponentScan(basePackages = "dev.lfb0801.pqm")
public class PqmConfiguration {

    private final PMAProperties pmaProperties;

    public PqmConfiguration(PMAProperties pmaProperties) {
        this.pmaProperties = pmaProperties;
    }

    @Bean
    public Repository repo() {
        Path foo = Path.of(TARGET_PATH.value);
        if (Files.exists(foo)) {
            try {
                Files.walk(foo)
                    .sorted(reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);

                Files.createDirectory(foo);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            return new RepositoryBuilder().setWorkTree(new File(foo.toString()))
                .setup()
                .build();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Bean
    public Git git(Repository repo) {
        Git git = new Git(repo);
        try {
            Git.cloneRepository()
                .setURI(pmaProperties.path())
                .setDirectory(repo.getWorkTree())
                .call();
        } catch (GitAPIException e) {
            throw new IllegalArgumentException("Repository should already exist", e);
        }
        return git;
    }
}
