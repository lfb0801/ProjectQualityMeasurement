package dev.lfb0801.pqm.domain;

import java.util.function.Function;

import dev.lfb0801.pqm.service.CompareService;

public record Comparing<T>(T left, T right) {

    public <R> Comparing<R> map(Function<T, R> mapper) {
        return new Comparing<>(mapper.apply(left), mapper.apply(right));
    }
}
