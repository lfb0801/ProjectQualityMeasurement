package dev.lfb0801.pqm.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Index.atIndex;

import java.util.HashSet;

import org.assertj.core.api.Condition;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import dev.lfb0801.pqm.config.PqmConfiguration;
import dev.lfb0801.pqm.domain.Aggregate;
import dev.lfb0801.pqm.service.CompareService.Comparing;

@SpringBootTest(classes = PqmConfiguration.class)
class CompareServiceTest extends GitFSEnvironment {

    @Autowired
    private CompareService compareService;

    @Test
    void testNameToString() {
        assertThat(compareService.findTagMatching("release/1.0")).isNotNull()
            .isNotBlank();
    }

    @Test
    void testFlow() throws GitAPIException {
        local.tagList()
            .call()
            .forEach(ref -> System.out.println(ref.getName()));
        var input = new Comparing<>("release/0.0", "release/1.1");

        final var results = compareService.compareTags(input);

        assertThat(results)
            .has(new Condition<>(comparing -> comparing.equals(input), "First step"), atIndex(0))//
            .has(new Condition<>(comparing -> comparing.left() instanceof HashSet<Aggregate>, "Final step"), atIndex(results.size() - 1))//
        ;

        System.out.println();
    }
}
