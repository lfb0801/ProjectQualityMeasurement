package dev.lfb0801.pqm.service;

import static dev.lfb0801.pqm.domain.Aggregate.build;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.cthing.locc4j.Counts;
import org.cthing.locc4j.Language;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.function.ThrowingSupplier;
import org.springframework.stereotype.Service;

import dev.lfb0801.pqm.domain.Aggregate;

@Service
public class AggregateService {

    private final LOCScanner locScanner;
    private final GitScanner gitScanner;

    public AggregateService(LOCScanner locScanner, GitScanner gitScanner) {
        this.locScanner = locScanner;
        this.gitScanner = gitScanner;
    }

    public Set<Aggregate> aggregate() throws IOException {
        Set<String> files = gitScanner.getFilesInRepository();
        return files.stream()
            .map(file -> build(aggregate -> aggregate //
                .file(file)
                .locSrc(getLinesOfCodeFor(locScanner::scanSources))
                .locTest(getLinesOfCodeFor(locScanner::scanTests))
                .commits(getCommits(file))))
            .collect(Collectors.toSet());
    }

    private int getCommits(String file) {
        try {
            return gitScanner.countCommits(file)
                .getValue()
                .intValue();
        } catch (GitAPIException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int getLinesOfCodeFor(ThrowingSupplier<Map<Language, Counts>> scanResult) {
        try {
            return scanResult.get()
                .values()
                .stream()
                .mapToInt(Counts::getCodeLines)
                .sum();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

}
