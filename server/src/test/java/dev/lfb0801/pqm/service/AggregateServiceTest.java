package dev.lfb0801.pqm.service;

import static dev.lfb0801.pqm.Unchecked.Handlers.Suppress.suppress;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import dev.lfb0801.pqm.config.PqmConfiguration;

@SpringBootTest(classes = PqmConfiguration.class)
class AggregateServiceTest extends GitFSEnvironment {

    @Autowired
    private AggregateService aggregateService;

    @Test
    void testAggregateService() throws IOException {
        var result = aggregateService.aggregate(local.getRepository()
            .resolve(getInitialCommitRef()));

        assertThat(result)//
            .allMatch(a -> a.locSrc() == 6)
            .allMatch(a -> a.commits()
                               .size() == 1);
    }

    private String getInitialCommitRef() {
        return suppress().get(() -> StreamSupport.stream(local.log()
                .call()
                .spliterator(), false
            )
            .toList()
            .getLast()
            .getId()
            .getName());
    }
}
