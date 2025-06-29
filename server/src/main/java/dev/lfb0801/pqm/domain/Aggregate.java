package dev.lfb0801.pqm.domain;

import java.util.Set;
import java.util.function.Consumer;

import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record Aggregate(String file, int locSrc, int locTest, Set<String> commits) {

    public static Aggregate build(Consumer<AggregateBuilder> builder) {
        var initial = AggregateBuilder.builder();

        builder.accept(initial);

        return initial.build();
    }
}
