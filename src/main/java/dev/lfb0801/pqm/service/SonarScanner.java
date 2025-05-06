package dev.lfb0801.pqm.service;

import org.sonarsource.scanner.lib.ScannerEngineBootstrapper;

import java.util.Map;

public class SonarScanner {

    void usingScannerEngineBootstrapper() {
        ScannerEngineBootstrapper.create("", "")
                                 .addBootstrapProperties(Map.of())
                                 .bootstrap();
    }

    void usingDockerContainerOfSonarScannerCLI() {

    }
}
