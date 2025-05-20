package dev.lfb0801.pqm.service;

import static dev.lfb0801.pqm.util.Unchecked.uncheck;
import static org.assertj.core.api.Assertions.assertThat;
import static org.cthing.locc4j.Language.Java;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.cthing.locc4j.Counts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LOCScannerTest {

    @Test
    @DisplayName("Count lines of code in project and assert some conditions")
    void countLinesInThisProject() throws IOException {
        var path = getProjectRoot();

        var LOCScanner = new LOCScanner(path);

        var sources = Optional.ofNullable(LOCScanner.scanSources())
            .map(Map::values)
            .orElseThrow();
        var tests = Optional.ofNullable(LOCScanner.scanTests())
            .map(Map::values)
            .orElseThrow();

        var scans = Stream.concat(sources.stream(), tests.stream())
            .toList();
        assertThat(scans)//
            .allMatch(c -> c.getCodeLines() > c.getBlankLines() + c.getCommentLines());
    }

    private String getProjectRoot() {
        return Optional.of(this.getClass())
            .map(Class::getClassLoader)
            .map(cl -> cl.getResource("."))
            .map(uncheck((URL url) -> Path.of(url.toURI())))
            .map(Path::getParent)
            .map(Path::getParent)
            .map(Path::toFile)
            .map(File::getPath)
            .orElseThrow();
    }
}
