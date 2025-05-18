package dev.lfb0801.pqm;

import static dev.lfb0801.pqm.util.Unchecked.uncheck;

import java.nio.file.Path;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.FileSystemUtils;

@SpringBootApplication
public class Main {

    public static void main(String... args) {
        SpringApplication.run(Main.class, args);
        Runtime.getRuntime().addShutdownHook(new Thread(uncheck(() -> FileSystemUtils.deleteRecursively(Path.of(Constants.TARGET_PATH.value)))));}
}
