package dev.lfb0801.pqm.util;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("*")
public class Unchecked {

    public static <T> Consumer<T> uncheck(ThrowingConsumer<T> consumer) {
        return (t) -> {
            try {
                consumer.check(t);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        };
    }

    public static <T, U> BiConsumer<T, U> uncheck(ThrowingBiConsumer<T, U> biConsumer) {
        return (t, u) -> {
            try {
                biConsumer.check(t, u);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        };
    }

    public static Runnable uncheck(ThrowingRunnable runnable) {
        return () -> {
            try {
                runnable.check();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        };
    }

    public static <T, R> Function<T, R> uncheck(ThrowingFunction<T, R> function) {
        return t -> {
            try {
                return function.check(t);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        };
    }

    public static <T, U, R> BiFunction<T, U, R> uncheck(ThrowingBiFunction<T, U, R> function) {
        return (t, u) -> {
            try {
                return function.check(t, u);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        };
    }

    @FunctionalInterface
    public interface ThrowingRunnable {
        void check() throws Throwable;
    }

    @FunctionalInterface
    public interface ThrowingFunction<T, R> {
        R check(T t) throws Throwable;
    }

    @FunctionalInterface
    public interface ThrowingBiFunction<T, U, R> {
        R check(T t, U u) throws Throwable;
    }

    @FunctionalInterface
    public interface ThrowingConsumer<T> {
        void check(T t) throws Throwable;
    }

    @FunctionalInterface
    public interface ThrowingBiConsumer<T, V> {
        void check(T t, V v) throws Throwable;
    }
}
