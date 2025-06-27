package dev.lfb0801.pqm.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import dev.lfb0801.pqm.config.PqmConfiguration;
import dev.lfb0801.pqm.domain.Comparing;

@SpringBootTest(classes = PqmConfiguration.class)
class CompareServiceTest extends GitFSEnvironment {

    @Autowired
    private CompareService compareService;

    @Test
    void testFindTagMatching() {
        assertThat(compareService.findTagMatching("release/1.0")).isNotNull()
            .isNotBlank();
    }

    @Test
    void testFindTagMatching_fail() {
        assertThatThrownBy(() -> compareService.findTagMatching("non-existing-tag")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testFlow() throws GitAPIException {
        var input = new Comparing<>("release/0.0", "release/1.1");

        final var results = compareService.compareTags(input);

        assertThat(results).first()
            .isEqualTo(input);
        assertThat(results).last()
            .hasNoNullFieldsOrProperties();
    }
}
