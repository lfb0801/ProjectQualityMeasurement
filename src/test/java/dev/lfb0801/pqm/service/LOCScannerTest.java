package dev.lfb0801.pqm.service;

import static dev.lfb0801.pqm.Unchecked.uncheck;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import dev.lfb0801.pqm.config.PqmConfiguration;

@SpringBootTest(classes = PqmConfiguration.class)
class LOCScannerTest {

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("quality.target.path", () -> Paths.get("")
            .toAbsolutePath().toString());
    }

    @Autowired
    LOCScanner locScanner;

    @Test
    @DisplayName("Count lines of code in project and assert some conditions")
    void countLinesInThisProject() throws IOException {
        var sources = Optional.ofNullable(locScanner.scanSources())
            .map(Map::values)
            .orElseThrow();
        var tests = Optional.ofNullable(locScanner.scanTests())
            .map(Map::values)
            .orElseThrow();

        var scans = Stream.concat(sources.stream(), tests.stream())
            .toList();
        assertThat(scans)//
            .allMatch(c -> c.getCodeLines() > c.getBlankLines() + c.getCommentLines());
    }
}
