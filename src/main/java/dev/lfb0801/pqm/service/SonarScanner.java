package dev.lfb0801.pqm.service;

import java.util.Map;

import org.sonarsource.scanner.lib.ScannerEngineBootstrapper;

@SuppressWarnings("unused")
public class SonarScanner {

    void usingScannerEngineBootstrapper() {
        try (var result = ScannerEngineBootstrapper.create("", "")
            .addBootstrapProperties(Map.of())
            .bootstrap()) {
            result.getEngineFacade()
                .analyze(Map.of());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    void usingDockerContainerOfSonarScannerCLI() {

    }
}
