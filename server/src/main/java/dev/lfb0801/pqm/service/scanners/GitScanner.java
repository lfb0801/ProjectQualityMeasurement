package dev.lfb0801.pqm.service.scanners;

import static java.util.function.Predicate.not;
import static java.util.stream.StreamSupport.stream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.stereotype.Service;

@Service
public class GitScanner {

    private final Git git;

    public GitScanner(Git git) {
        this.git = git;
    }

    public Set<String> getFilesInRepository(ObjectId objectId) throws IOException, GitAPIException {
        git.checkout()
            .setName(objectId.getName())
            .call();
        return listFilesInCommit();
    }

    public Set<String> getCommitsContainingFile(String file) throws GitAPIException, IOException {
        final var repo = git.getRepository();
        return Set.copyOf(stream(git.log()
                                     .add(repo.resolve("HEAD"))
                                     .addPath(file)
                                     .call()
                                     .spliterator(), false
        ).map(RevCommit::getFirstMessageLine)
                              .toList()
                              .reversed());
    }

    private Set<String> listFilesInCommit() throws IOException {
        return Files.walk(git.getRepository()
                              .getWorkTree()
                              .toPath())
            .filter(Files::isRegularFile)
            .filter(not((path -> path.toString()
                .contains(".git"))))
            .map(path -> git.getRepository().getWorkTree().toPath().relativize(path))
            .map(Path::toString)
            .collect(Collectors.toSet());
    }
}
