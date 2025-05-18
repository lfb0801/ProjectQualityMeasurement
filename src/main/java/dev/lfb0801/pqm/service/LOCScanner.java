package dev.lfb0801.pqm.service;

import org.cthing.locc4j.CountUtils;
import org.cthing.locc4j.CountingTreeWalker;
import org.cthing.locc4j.Counts;
import org.cthing.locc4j.Language;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

@Service
public class LOCScanner {

    private final String path;

    public LOCScanner(@Value("${quality.target.path}") String path) {
        this.path = path;
    }

    private Map<Language, Counts> matchPattern(String pattern) throws IOException {
        final var walker = //
                new CountingTreeWalker(Path.of(path), pattern)//
                                                              .respectGitignore(true)
                                                              .countDocStrings(false);
        final Map<Path, Map<Language, Counts>> counts = walker.count();
        return CountUtils.byLanguage(counts);
    }

    public Map<Language, Counts> scanSources() throws IOException {
        return matchPattern("**/src/main/**");
    }

    public Map<Language, Counts> scanTests() throws IOException {
        return matchPattern("**/src/test/**");
    }
}
