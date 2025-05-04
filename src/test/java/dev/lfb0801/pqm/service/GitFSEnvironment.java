package dev.lfb0801.pqm.service;

import dev.lfb0801.pqm.util.Unchecked;
import dev.lfb0801.pqm.util.Unchecked.ThrowingConsumer;
import org.assertj.core.util.Files;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static dev.lfb0801.pqm.util.Unchecked.uncheck;

public abstract class GitFSEnvironment {


    public static final Git remote = initRepository();
    public static final Git local = cloneRepository();
    private static final ThrowingConsumer<String> commitState = (String s) -> {
        local.add()
             .addFilepattern(".")
             .call();

        local.commit()
             .setMessage(s)
             .call();
    };
    public static final String MASTER = "refs/heads/master";
    public static final String ORIGIN_MASTER = "refs/remotes/origin/master";

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

    public static void doAndCommit(String message, Runnable runnable) {
        runnable.run();
        uncheck(commitState).accept(message);
    }

    public static Unchecked.ThrowingBiConsumer<String, String> writeToFile() {
        return (String file, String content) -> new FileOutputStream(file).write(content.getBytes());
    }

    public void tearDown() {
        remote.close();
        local.close();
    }

    public File createFile(String name) {
        File file = new File(local.getRepository()
                                  .getWorkTree(), name);
        try {
            if (!file.createNewFile()) {
                throw new IllegalStateException();
            }
        } catch (IOException e) {
            throw new IllegalStateException();
        }
        return file;
    }
}
