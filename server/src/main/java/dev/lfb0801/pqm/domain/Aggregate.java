package dev.lfb0801.pqm.domain;

import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import org.cthing.locc4j.Counts;
import org.cthing.locc4j.Language;

import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record Aggregate(String file, Map<Path, Map<Language, Counts>> locSrc, Map<Path, Map<Language, Counts>> locTest, Set<String> commits) {

    public static Aggregate build(Consumer<AggregateBuilder> builder) {
        var initial = AggregateBuilder.builder();

        builder.accept(initial);

        return initial.build();
    }
}
