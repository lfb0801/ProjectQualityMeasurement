package dev.lfb0801.pqm.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static dev.lfb0801.pqm.util.Unchecked.uncheck;
import static org.assertj.core.api.Assertions.assertThat;

class GitCounterTest extends GitFSEnvironment {

    private final GitCounter gitCounter = new GitCounter(local);

    @Test
    @Order(0)
    @DisplayName("If the second commit removes 1 of the files, then only the remaining file will be returned")
    void getEveryCommitedFileOnlyReturnsExistingFiles() throws IOException {
        String deletedFile = "test.txt";
        String remainingFile = "test2.txt";

        doAndCommit("initial commit", uncheck(() -> {
            createFile(deletedFile);
            createFile(remainingFile);

            uncheck(writeToFile())
                    .accept(deletedFile, "Hello World!");
        }));

        doAndCommit("second commit", uncheck(() -> {
            new File(local.getRepository()
                          .getWorkTree(), deletedFile).delete();
        }));

        assertThat(gitCounter.getEveryCommittedFile())
                .contains(remainingFile)
                .doesNotContain(deletedFile);

    }
}
