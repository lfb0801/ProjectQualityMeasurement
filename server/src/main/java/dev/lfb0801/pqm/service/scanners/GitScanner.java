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

import dev.lfb0801.pqm.config.PMAProperties;

@Service
public class GitScanner {

    private final Git git;
    private final PMAProperties pmaProperties;

    public GitScanner(Git git, PMAProperties pmaProperties) {
        this.git = git;
        this.pmaProperties = pmaProperties;
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
            //  Adding a path to a log-command will only log commits that relate to the given path.
            .addPath(file)
            .call()
            .spliterator(), false
        )//
            // Commits by authors (like bots) shouldn't be part of counted
            .filter(commit -> pmaProperties.blockedAuthors()
                .contains(commit.getAuthorIdent()
                    .getEmailAddress()))
            .map(RevCommit::getFirstMessageLine)
            .toList()
            .reversed());
    }

    private Set<String> listFilesInCommit() throws IOException {
        return Files.walk(git.getRepository()
                .getWorkTree()
                .toPath())
            .filter(Files::isRegularFile)
            //  Specifically .git files shouldn't ever be included
            .filter(not((path -> path.toString()
                .contains(".git"))))
            //  We want the path relative from the root of the project,
            //  to allow for multiple files with the same name
            .map(path -> git.getRepository()
                .getWorkTree()
                .toPath()
                .relativize(path))
            //  Only analyse maven modules, by looking for the pom.xml files
            .filter(path -> "pom.xml".equals(path.getFileName()
                .toString()))
            .map(Path::getParent)
            .map(Path::toString)
            .collect(Collectors.toSet());
    }
}
