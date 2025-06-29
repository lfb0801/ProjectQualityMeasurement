package dev.lfb0801.pqm.service.scanners;

import static dev.lfb0801.pqm.Unchecked.Handlers.Suppress.suppress;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Function;

import org.cthing.locc4j.CountingTreeWalker;
import org.cthing.locc4j.Counts;
import org.cthing.locc4j.Language;
import org.springframework.stereotype.Service;

@Service
public class LOCScanner {

    public Map<Path, Map<Language, Counts>> scanSources(Path path) throws IOException {
        return matchPattern(path).apply("**/src/main/**");
    }

    public Map<Path, Map<Language, Counts>> scanTests(Path path) throws IOException {
        return matchPattern(path).apply("**/src/test/**");
    }

    private Function<String, Map<Path, Map<Language, Counts>>> matchPattern(Path path) {
        return suppress().function(pattern -> new CountingTreeWalker(path, pattern)//
            .respectGitignore(true)
            .countDocStrings(false)
            .count());
    }
}
