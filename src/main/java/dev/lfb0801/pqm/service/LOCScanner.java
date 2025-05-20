package dev.lfb0801.pqm.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import org.cthing.locc4j.CountUtils;
import org.cthing.locc4j.CountingTreeWalker;
import org.cthing.locc4j.Counts;
import org.cthing.locc4j.Language;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LOCScanner {

    private final String path;

    public LOCScanner(@Value("${quality.target.path}") String path) {
        this.path = path;
    }

    public Map<Path, Counts> scanSources() throws IOException {
        return matchPattern("**/src/main/**");
    }

    public Map<Path, Counts> scanTests() throws IOException {
        return matchPattern("**/src/test/**");
    }

    private Map<Path, Counts> matchPattern(String pattern) throws IOException {
        var walker = //
            new CountingTreeWalker(Path.of(path), pattern)
            .respectGitignore(true)
            .countDocStrings(false);

        Map<Path, Map<Language, Counts>> counts = walker.count();

        return CountUtils.byFile(counts);
    }
}
