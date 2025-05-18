package dev.lfb0801.pqm.service;

import static java.util.Comparator.comparing;

import java.io.IOException;
import java.util.Comparator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import dev.lfb0801.pqm.config.PqmConfiguration;
import dev.lfb0801.pqm.domain.Aggregate;

@SpringBootTest(classes = PqmConfiguration.class, properties = "quality.target.path=/Users/lloydvanzaalen/School/ProjectQualityMeasurement/.git")
class AggregateServiceTest {

    @Autowired
    private AggregateService aggregateService;

    @Test
    void testAggregateService() throws IOException {
        var result = aggregateService.aggregate();
        result.stream()
            .sorted(comparing(Aggregate::commits))
            .forEach(System.out::println);
    }
}
