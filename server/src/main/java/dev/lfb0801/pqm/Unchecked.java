package dev.lfb0801.pqm;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.springframework.validation.Errors;

import dev.lfb0801.pqm.Unchecked.Handlers.Log;
import dev.lfb0801.pqm.Unchecked.Handlers.Rethrow;
import dev.lfb0801.pqm.Unchecked.Handlers.Suppress;

public final class Unchecked {

    interface Throwing {

        @FunctionalInterface
        interface Runnable {

            void run() throws Throwable;
        }

        @FunctionalInterface
        interface Supplier<T> {

            T get() throws Throwable;
        }

        @FunctionalInterface
        interface Consumer<T> {

            void accept(T t) throws Throwable;
        }

        @FunctionalInterface
        interface Function<T, Result> {

            Result apply(T t) throws Throwable;
        }

        @FunctionalInterface
        interface Predicate<T> {

            boolean test(T t) throws Throwable;
        }

        @FunctionalInterface
        interface BiConsumer<T, U> {

            void accept(T t, U u) throws Throwable;
        }

        @FunctionalInterface
        interface BiFunction<T, U, Result> {

            Result apply(T t, U u) throws Throwable;
        }

        @FunctionalInterface
        interface BiPredicate<T, U> {

            boolean accept(T t, U u) throws Throwable;
        }
    }

    static class Handlers {

        public static final class Suppress extends Errors {

            public static Suppress suppress() {
                return new Suppress();
            }

            @Override
            protected void handler(Throwable throwable) {

            }
        }

        public static final class Rethrow extends Errors {

            public static Rethrow rethrow() {
                return new Rethrow();
            }

            @Override
            protected void handler(Throwable throwable) {
                if (throwable instanceof RuntimeException) {
                    throw (RuntimeException) throwable;
                } else {
                    throw new RuntimeException(throwable);
                }
            }
        }

        public static final class Log extends Errors {

            private final Logger logger;

            private Log(Logger logger) {
                this.logger = logger;
            }

            public static Log log(Logger logger) {
                return new Log(logger);
            }

            @Override
            protected void handler(Throwable throwable) {
                logger.error("An error occurred", throwable);
            }
        }
    }

    sealed abstract static class Errors permits Log, Rethrow, Suppress {

        protected abstract void handler(Throwable throwable);

        public void run(Throwing.Runnable runnable) {
            runnable(runnable).run();
        }

        public <Result> Result get(Throwing.Supplier<Result> supplier) {
            return supplier(supplier).get();
        }

        public <Input, Result> Result apply(Throwing.Function<Input, Result> function, Input input) {
            return function(function).apply(input);
        }

        public <Input> void accept(Throwing.Consumer<Input> consumer, Input input) {
            consumer(consumer).accept(input);
        }

        public <Input> boolean test(Throwing.Predicate<Input> predicate, Input input) {
            return predicate(predicate).test(input);
        }

        public <Input> boolean negate(Throwing.Predicate<Input> predicate, Input input) {
            return predicate(predicate).negate()
                .test(input);
        }

        public java.lang.Runnable runnable(Throwing.Runnable runnable) {
            return () -> {
                try {
                    runnable.run();
                } catch (Throwable e) {
                    handler(e);
                }
            };
        }

        public <Input> Consumer<Input> consumer(Throwing.Consumer<Input> consumer) {
            return val -> {
                try {
                    consumer.accept(val);
                } catch (Throwable e) {
                    handler(e);
                }
            };
        }

        public <Result> Supplier<Result> supplier(Throwing.Supplier<Result> supplier) {
            return () -> {
                try {
                    return supplier.get();
                } catch (Throwable e) {
                    handler(e);
                }
                return null;
            };
        }

        public <Input, Result> Function<Input, Result> function(Throwing.Function<Input, Result> function) {
            return input -> {
                try {
                    return function.apply(input);
                } catch (Throwable e) {
                    handler(e);
                    return null;
                }
            };
        }

        public <Input> Predicate<Input> predicate(Throwing.Predicate<Input> predicate) {
            return input -> {
                try {
                    return predicate.test(input);
                } catch (Throwable e) {
                    handler(e);
                    return false;
                }
            };
        }
    }
}
