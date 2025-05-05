package dev.lfb0801.pqm.service;

import dev.lfb0801.pqm.util.Unchecked;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static dev.lfb0801.pqm.util.Unchecked.uncheck;
import static java.util.stream.Stream.concat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

class GitCounterTest extends GitFSEnvironment {


    @Test
    @DisplayName("If the second commit removes 1 of the files, then only the remaining file will be returned")
    void happyFlow() throws IOException {

        GitCounter gitCounter = new GitCounter(local);
        final String deletedFile = "deleted.txt";
        final String remainingFile = "remaining.txt";

        performCommits(List.of(
                () -> {
                    createFile(deletedFile);
                    createFile(remainingFile);

                    writeToFile(deletedFile, "Hello World!");
                },
                () -> {
                    writeToFile(remainingFile, "Salve Mundi!");
                },
                () -> {
                    new File(local.getRepository()
                                  .getWorkTree(), deletedFile).delete();
                }
        ));

        var everyCommittedFile = gitCounter.getFilesInRepository();
        var commitCountPerFile = Stream.of(remainingFile, deletedFile)
                                       .map(uncheck(gitCounter::countCommits))
                                       .collect(Collectors.toMap(Map.Entry::getKey,
                                                                 Map.Entry::getValue
                                       ));

        assertThat(everyCommittedFile)
                .contains(remainingFile)
                .doesNotContain(deletedFile);

        assertThat(commitCountPerFile.entrySet())
                .contains(
                        entry(remainingFile, 3L),
                        entry(deletedFile, 2L)
                );
    }

    @Test
    @DisplayName("Let's test if my implementation breaks when there are a lot of commits")
    void loadTest() throws IOException {

        GitCounter gitCounter = new GitCounter(local);
        final String file = "remaining.txt";

        performCommits(concat(
                Stream.of(
                        () -> {
                            createFile(file);
                        }
                ),
                IntStream.range(1, 1000)
                         .mapToObj(i -> (Unchecked.ThrowingRunnable) () -> {
                             appendToFile(file, String.valueOf(i));
                         })
        ).toList());

        var everyCommittedFile = gitCounter.getFilesInRepository();
        var commitCountPerFile = Stream.of(file)
                                       .map(uncheck(gitCounter::countCommits))
                                       .collect(Collectors.toMap(Map.Entry::getKey,
                                                                 Map.Entry::getValue
                                       ));

        assertThat(everyCommittedFile)
                .contains(file);

        assertThat(commitCountPerFile.entrySet())
                .contains(
                        entry(file, 1000L)
                );
    }
}
