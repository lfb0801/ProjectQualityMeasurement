package dev.lfb0801.pqm.service;

import static dev.lfb0801.pqm.util.Unchecked.uncheck;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.assertj.core.util.Files;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import dev.lfb0801.pqm.util.Unchecked;
import dev.lfb0801.pqm.util.Unchecked.ThrowingConsumer;

public abstract class GitFSEnvironment {

    public static Git remote;
    public static Git local;

    private static final ThrowingConsumer<String> commitState = (String s) -> {
        local.add()
            .setAll(true)
            .call();

        local.commit()
            .setMessage(s)
            .call();
    };

    private static Git initRepository() {
        try {
            return Git.init()
                .setDirectory(Files.newTemporaryFolder())
                .call();
        } catch (GitAPIException e) {
            throw new IllegalStateException(e);
        }
    }

    private static Git cloneRepository() {
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

    public static void writeToFile(String relativePath, String content) throws IOException {
        File file = new File(local.getRepository().getWorkTree(), relativePath);
        try (var fos = new FileOutputStream(file)) {
            fos.write(content.getBytes(StandardCharsets.UTF_8));
        }
    }

    public static void appendToFile(String relativePath, String content) throws IOException {
        File file = new File(local.getRepository().getWorkTree(), relativePath);
        String old = file.exists() ? Files.contentOf(file, StandardCharsets.UTF_8) : "";
        try (var fos = new FileOutputStream(file)) {
            fos.write((old + "\n" + content).getBytes(StandardCharsets.UTF_8));
        }
    }

    @BeforeEach
    void setup() {
        remote = initRepository();
        local = cloneRepository();
    }

    @AfterEach
    void tearDown() {
        remote.close();
        local.close();
    }

    public void performCommits(List<Unchecked.ThrowingRunnable> commits) {
        IntStream.range(0, commits.size())
            .mapToObj(i -> Map.entry("commit: %s".formatted(i), commits.get(i)))
            .forEach(e -> {
                uncheck(e.getValue()).run();
                uncheck(commitState).accept(e.getKey());
            });
    }

    public File createFile(String name) throws IOException {
        File file = new File(local.getRepository()
                                 .getWorkTree()
                                 .getCanonicalPath(), name
        );
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new IllegalStateException();
        }
        return file;
    }
}
