package dev.lfb0801.pqm.service;

import org.cthing.locc4j.Counts;
import org.cthing.locc4j.Language;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class DirectoryCounter {

    public Map<Language, Counts> countDirectory(Path path) throws IOException {
        Files.walk(path, 1)
             .filter(Files::isRegularFile)
             .forEach(System.out::println);
        return Map.of();
    }
}
