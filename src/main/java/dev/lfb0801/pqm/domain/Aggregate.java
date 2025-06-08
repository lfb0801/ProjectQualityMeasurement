package dev.lfb0801.pqm.domain;

import io.soabase.recordbuilder.core.RecordBuilder;

import java.util.Set;
import java.util.function.Consumer;

@RecordBuilder
public record Aggregate(String file, int locSrc, int locTest, Set<String> commits) {

    public static Aggregate build(Consumer<AggregateBuilder> builder) {
        var initial = AggregateBuilder.builder();

        builder.accept(initial);

        return initial.build();
    }
}
