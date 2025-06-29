package dev.lfb0801.pqm.service;

import static dev.lfb0801.pqm.Unchecked.Handlers.Suppress.suppress;
import static dev.lfb0801.pqm.domain.Aggregate.build;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.springframework.stereotype.Service;

import dev.lfb0801.pqm.domain.Aggregate;
import dev.lfb0801.pqm.service.scanners.GitScanner;
import dev.lfb0801.pqm.service.scanners.LOCScanner;

@Service
public class AggregateService {

    private final LOCScanner locScanner;
    private final GitScanner gitScanner;
    private final Git git;

    public AggregateService(LOCScanner locScanner, GitScanner gitScanner, Git git) {
        this.locScanner = locScanner;
        this.gitScanner = gitScanner;
        this.git = git;
    }

    public Set<Aggregate> aggregate(ObjectId tag) {
        try {
            Set<String> files = gitScanner.getModulesInRepository(tag);
            return files.stream()
                .map(file -> {
                    var dir = new File(git.getRepository().getWorkTree(), file).toPath().getParent();
                    var rel = git.getRepository().getWorkTree().toPath().relativize(dir);
                    return build(aggregate -> aggregate //
                        .file(file)
                        .locSrc(suppress().get(() -> locScanner.scanSources(rel)))
                        .locTest(suppress().get(() -> locScanner.scanTests(rel)))
                        .commits(getCommits(file)));
                })
                .collect(Collectors.toSet());
        } catch (IOException | GitAPIException e) {
            throw new IllegalStateException("Some problem occurred while aggregating", e);
        }
    }

    private Set<String> getCommits(String file) {
        try {
            return gitScanner.getCommitsContainingFile(file);
        } catch (GitAPIException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
