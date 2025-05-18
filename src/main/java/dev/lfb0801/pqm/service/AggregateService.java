package dev.lfb0801.pqm.service;

import dev.lfb0801.pqm.domain.Aggregate;
import org.cthing.locc4j.Counts;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

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
                    .map(file -> {
                        try {
                            int locTest = locScanner.scanTests()
                                                    .values()
                                                    .stream()
                                                    .mapToInt(Counts::getTotalLines)
                                                    .sum();
                            int locSrc = locScanner.scanSources()
                                                   .values()
                                                   .stream()
                                                   .mapToInt(Counts::getTotalLines)
                                                   .sum();
                            int commits = gitScanner.countCommits(file)
                                                    .getValue()
                                                    .intValue();
                            return new Aggregate(file, locTest, locSrc, commits);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toSet());
    }
}
