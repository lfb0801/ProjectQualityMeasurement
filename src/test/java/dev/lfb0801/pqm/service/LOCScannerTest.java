package dev.lfb0801.pqm.service;

import static dev.lfb0801.pqm.util.Unchecked.uncheck;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

import org.cthing.locc4j.Counts;
import org.cthing.locc4j.Language;
import org.junit.jupiter.api.Test;

class LOCScannerTest {

    @Test
    void countLinesInThisProject() throws IOException {
        String path = getProjectRoot();

        LOCScanner LOCScanner = new LOCScanner(path);

        Map<Language, Counts> sources = LOCScanner.matchPattern("**/src/main/**");
        Map<Language, Counts> tests = LOCScanner.matchPattern("**/src/test/**");

        Counts javaSource = Optional.ofNullable(sources.getOrDefault(Language.Java, null))
            .orElseThrow(IllegalStateException::new);
        Counts javaTest = Optional.ofNullable(tests.getOrDefault(Language.Java, null))
            .orElseThrow(IllegalStateException::new);

        System.out.println("javaSource = " + javaSource);
        System.out.println("javaTest = " + javaTest);

        assertThat(javaSource).matches(c -> c.getCommentLines() == 0, "Source code should not have comments")
            .matches(c -> c.getBlankLines() < c.getCodeLines(), "There should not be more blank than source lines")
            .matches(c -> c.getCodeLines() < javaTest.getCodeLines(), "Should have more test than source code");
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
