package dev.lfb0801.pqm.service;

import static dev.lfb0801.pqm.Unchecked.Handlers.Suppress.suppress;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.assertj.core.util.Files;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

public abstract class GitFSEnvironment {

    public static Git remote;
    public static Git local;
    private static Path tempRepoPath;

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        setup(); // initializes GitFSEnvironment (remote + local)

        tempRepoPath = local.getRepository()
            .getWorkTree()
            .toPath();

        suppress()
            .run(() -> {
            createMockFile(0, 0);
            createMockFile(0, 1);

            local.add()
                .setAll(true)
                .call();

            local.commit()
                .setMessage("initial commit")
                .call();
            local.tag()
                .setObjectId(local.log()
                                 .setMaxCount(1)
                                 .call()
                                 .iterator()
                                 .next())
                .setName("release/0.0")
                .setForceUpdate(false)
                .call();
        });
        commitNewFileAndRelease(1, 0);
        commitNewFileAndRelease(1, 1);

        registry.add("quality.target.path", () -> tempRepoPath.toString());
    }

    private static void commitNewFileAndRelease(int module, int file) {
        suppress()
            .run(() -> {
            createMockFile(module, file);

            local.add()
                .setAll(true)
                .call();

            local.commit()
                .setMessage("committing:" + module + "." + file)
                .call();

            local.tag()
                .setName("release/%s.%s".formatted(module, file))
                .setObjectId(local.log()
                                 .setMaxCount(1)
                                 .call()
                                 .iterator()
                                 .next())
                .setForceUpdate(false)
                .call();
        });
    }

    private static void createMockFile(int i, int j) throws IOException {
        String className = "Class" + j + ".java";
        String packageName = "com/example/module" + i;
        String path = "src/main/java/" + packageName + "/" + className;
        appendToFile(path, generateFakeJavaClass(className, i, j));
    }

    private static String generateFakeJavaClass(String className, int module, int index) {
        return """
            package com.example.module%d;
            
            public class %s {
                public void doSomething() {
                    System.out.println("Hello from %s #%d");
                }
            }
            """.formatted(module, className.replace(".java", ""), className.replace(".java", ""), index);
    }

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

    public static void appendToFile(String relativePath, String content) throws IOException {
        File file = new File(local.getRepository()
                                 .getWorkTree(), relativePath
        );
        File parent = file.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }
        String old = file.exists() ? Files.contentOf(file, StandardCharsets.UTF_8) : "";
        try (var fos = new FileOutputStream(file)) {
            fos.write((old + "\n" + content).getBytes(StandardCharsets.UTF_8));
        }
    }

    @BeforeAll
    static void setup() {
        remote = initRepository();
        local = cloneRepository();
    }

    @AfterAll
    static void tearDown() {
        remote.close();
        local.close();
    }

    public static void performCommits(List<Runnable> commits) {
        IntStream.range(0, commits.size())
            .mapToObj(i -> Map.entry("commit: %s".formatted(i), commits.get(i)))
            .forEach(e -> {
                e.getValue()
                    .run();
                suppress()
                    .accept((String s) -> {
                                    local.add()
                                        .setAll(true)
                                        .call();

                                    local.commit()
                                        .setMessage(s)
                                        .call();
                                }, e.getKey()
                );
            });
    }

    @Bean
    Git git() {
        return local;
    }
}
