package dev.lfb0801.pqm.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.assertj.core.util.Files;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import dev.lfb0801.pqm.config.PMAProperties;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    Git local(Git remote) {
        try {
            String remoteUri = remote.getRepository()
                .getDirectory()
                .getCanonicalPath();
            File localDir = Files.newTemporaryFolder();

            return Git.cloneRepository()
                .setURI(remoteUri)
                .setDirectory(localDir)
                .call();
        } catch (GitAPIException | IOException exception) {
            throw new IllegalStateException();
        }
    }

    @Bean
    Git remote() {
        try {
            return Git.init()
                .setDirectory(Files.newTemporaryFolder())
                .call();
        } catch (GitAPIException e) {
            throw new IllegalStateException(e);
        }
    }

    @Bean
    PMAProperties pmaProperties(Git local) {
        Path tempRepoPath = local.getRepository()
            .getWorkTree()
            .toPath();
        return new PMAProperties(tempRepoPath.toString(), List.of());
    }
}
