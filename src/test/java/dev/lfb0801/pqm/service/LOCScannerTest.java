package dev.lfb0801.pqm.service;

import static dev.lfb0801.pqm.util.Unchecked.uncheck;
import static org.assertj.core.api.Assertions.assertThat;
import static org.cthing.locc4j.Language.Java;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LOCScannerTest {

    @Test
    @DisplayName("Count lines of code in project and assert some conditions")
    void countLinesInThisProject() throws IOException {
        var path = getProjectRoot();

        var LOCScanner = new LOCScanner(path);

        var sources = Optional.ofNullable(LOCScanner.scanSources())
            .map(s -> s.get(Java))
            .orElseThrow();
        var tests = Optional.ofNullable(LOCScanner.scanTests())
            .map(s -> s.get(Java))
            .orElseThrow();

        assertThat(sources).matches(c -> c.getCommentLines() == 0, "Source code should not have comments")
            .matches(c -> c.getBlankLines() < c.getCodeLines(), "There should not be more blank than source lines")
            .matches(c -> c.getCodeLines() < tests.getCodeLines(), "Should have more test than source code");
    }

    private String getProjectRoot() {
        return Optional.of(this.getClass())
            .map(Class::getClassLoader)
            .map(cl -> cl.getResource(""))
            .map(uncheck((URL url) -> Path.of(url.toURI())))
            .map(Path::getParent)
            .map(Path::getParent)
            .map(Path::toFile)
            .map(File::getPath)
            .orElseThrow();
    }
}
