package dev.lfb0801.pqm.service;

import static dev.lfb0801.pqm.Unchecked.Handlers.Suppress.suppress;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AggregateServiceTest extends GitFSEnvironment {

    @Autowired
    private AggregateService aggregateService;

    @Test
    void testAggregateServiceOnInitialCommit() throws IOException {
        var result = aggregateService.aggregate(git.getRepository()
            .resolve(getInitialCommitRef()));

        assertThat(result)//
            .isNotEmpty()
            .allMatch(a -> a.commits()
                               .size() == 1);
    }

    private String getInitialCommitRef() {
        return suppress().get(() -> StreamSupport.stream(git.log()
                .call()
                .spliterator(), false
            )
            .toList()
            .getLast()
            .getId()
            .getName());
    }
}
