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
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(classes = TestConfig.class)
public abstract class GitFSEnvironment {

    @Autowired
    public Git git;

    @BeforeEach
    void beforeEach() {
        suppress().run(() -> {
            createModule();
            createMockFile(0, 0);
            createMockFile(0, 1);

            git.add().setAll(true).call();

            git.commit().setMessage("initial commit").call();
            git.tag()
                .setObjectId(git.log().setMaxCount(1).call().iterator().next())
                .setName("release/0.0")
                .setForceUpdate(false)
                .call();
        });
        commitNewFileAndRelease(1, 0);
        commitNewFileAndRelease(1, 1);
    }

    private void commitNewFileAndRelease(int module, int file) {
        suppress().run(() -> {
            createMockFile(module, file);
            git.add().setAll(true).call();
            git.commit().setMessage("committing:" + module + "." + file).call();
            git.tag().setName("release/%s.%s".formatted(module, file))
                .setObjectId(git.log().setMaxCount(1).call().iterator().next())
                .setForceUpdate(false)
                .call();
        });
    }

    private void createMockFile(int i, int j) throws IOException {
        String className = "Class" + j + ".java";
        String packageName = "com/example/module" + i;
        String path = "src/main/java/" + packageName + "/" + className;
        appendToFile(path, generateFakeJavaClass(className, i, j));
    }

    private void createModule() throws IOException {
        appendToFile("pom.xml", "<inside of a pom file>");
    }

    private String generateFakeJavaClass(String className, int module, int index) {
        return """
            package com.example.module%d;
            
            public class %s {
                public void doSomething() {
                    System.out.println("Hello from %s #%d");
                }
            }
            """.formatted(module, className.replace(".java", ""), className.replace(".java", ""), index);
    }

    public void appendToFile(String relativePath, String content) throws IOException {
        File file = new File(git.getRepository()
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

    public void performCommits(List<Runnable> commits) {
        IntStream.range(0, commits.size())
            .mapToObj(i -> Map.entry("commit: %s".formatted(i), commits.get(i)))
            .forEach(e -> {
                e.getValue()
                    .run();
                suppress().accept((String s) -> {
                        git.add()
                            .setAll(true)
                            .call();

                        git.commit()
                            .setMessage(s)
                            .call();
                    }, e.getKey()
                );
            });
    }
}
