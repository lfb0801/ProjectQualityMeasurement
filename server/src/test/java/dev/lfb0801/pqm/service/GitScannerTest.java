package dev.lfb0801.pqm.service;

import static java.util.stream.Stream.concat;

import static dev.lfb0801.pqm.Unchecked.Errors.*;
import static dev.lfb0801.pqm.Unchecked.Handlers.Suppress.suppress;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dev.lfb0801.pqm.Unchecked.Errors;
import dev.lfb0801.pqm.Unchecked.Handlers.Suppress;

class GitScannerTest extends GitFSEnvironment {

    @Test
    @DisplayName("If the second commit removes 1 of the files, then only the remaining file will be returned")
    void happyFlow() throws IOException, GitAPIException {

        GitScanner gitScanner = new GitScanner(local);
        final String deletedFile = "deleted.txt";
        final String remainingFile = "remaining.txt";

        performCommits(List.of(suppress()
                                   .runnable(() -> {
                                   appendToFile(deletedFile, "");
                                   appendToFile(remainingFile, "");

                                   appendToFile(deletedFile, "Hello World!");
                               }), suppress()
                                   .runnable(() -> {
                                   appendToFile(remainingFile, "Salve Mundi!");
                               }), suppress()
                                   .runnable(() -> {
                                   new File(local.getRepository()
                                                .getWorkTree(), deletedFile
                                   ).delete();
                               })
        ));

        var everyCommittedFile = gitScanner.getFilesInRepository(local.getRepository()
                                                                     .resolve(Constants.HEAD));
        var commitCountPerFile = Stream.of(remainingFile, deletedFile)
            .map(suppress()
                     .function(file -> Map.entry(file, gitScanner.getCommitsContainingFile(file))))
            .collect(Collectors.toMap(Map.Entry::getKey,
                                      set -> set.getValue()
                                          .size()
            ));

        assertThat(everyCommittedFile).contains(remainingFile)
            .doesNotContain(deletedFile);

        assertThat(commitCountPerFile.entrySet()).contains(entry(remainingFile, 2), entry(deletedFile, 2));
    }

    @Test
    @DisplayName("Let's test if my implementation breaks when there are a lot of commits")
    void loadTest() throws IOException, GitAPIException {

        GitScanner gitScanner = new GitScanner(local);
        final String file = "new.txt";

        performCommits(concat(Stream.of(suppress()
                                            .runnable(() -> {
                                  appendToFile(file, "");
                              })),
                              IntStream.range(1, 1000)
                                  .mapToObj(i -> suppress()
                                      .runnable(() -> {
                                      appendToFile(file, String.valueOf(i));
                                  }))
        ).toList());

        var everyCommittedFile = gitScanner.getFilesInRepository(local.getRepository()
                                                                     .resolve(Constants.HEAD));
        var commitCountPerFile = Stream.of(file)
            .map(suppress()
                     .function(f -> Map.entry(f, gitScanner.getCommitsContainingFile(f))))
            .collect(Collectors.toMap(Map.Entry::getKey,
                                      set -> set.getValue()
                                          .size()
            ));

        assertThat(everyCommittedFile).contains(file);

        assertThat(commitCountPerFile.entrySet()).contains(entry(file, 1000));
    }
}
