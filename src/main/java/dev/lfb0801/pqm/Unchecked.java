package dev.lfb0801.pqm;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@SuppressWarnings("*")
public interface Unchecked {
	interface Throwing {

		interface Specific {
			@FunctionalInterface
			interface Runnable<E extends Throwable> {
				void run() throws E;
			}

			@FunctionalInterface
			interface Supplier<T, E extends Throwable> {
				T get() throws E;
			}

			@FunctionalInterface
			interface Consumer<T, E extends Throwable> {
				void accept(T t) throws E;
			}

			@FunctionalInterface
			interface Function<T, R, E extends Throwable> {
				R apply(T t) throws E;
			}

			@FunctionalInterface
			interface Predicate<T, E extends Throwable> {
				boolean test(T t) throws E;
			}

			@FunctionalInterface
			interface BiConsumer<T, U, E extends Throwable> {
				void accept(T t, U u) throws E;
			}

			@FunctionalInterface
			interface BiFunction<T, U, R, E extends Throwable> {
				R apply(T t, U u) throws E;
			}

			@FunctionalInterface
			interface BiPredicate<T, U, E extends Throwable> {
				boolean accept(T t, U u) throws E;
			}
		}

		@FunctionalInterface
		interface Runnable extends Specific.Runnable<Throwable> {
		}

		@FunctionalInterface
		interface Supplier<T> extends Specific.Supplier<T, Throwable> {
		}

		@FunctionalInterface
		interface Consumer<T> extends Specific.Consumer<T, Throwable> {
		}

		@FunctionalInterface
		interface Function<T, R> extends Specific.Function<T, R, Throwable> {
		}

		@FunctionalInterface
		interface Predicate<T> extends Specific.Predicate<T, Throwable> {
		}

		@FunctionalInterface
		interface BiConsumer<T, U> extends Specific.BiConsumer<T, U, Throwable> {
		}

		@FunctionalInterface
		interface BiFunction<T, U, R> extends Specific.BiFunction<T, U, R, Throwable> {
		}

		@FunctionalInterface
		interface BiPredicate<T, U> extends Specific.BiPredicate<T, U, Throwable> {
		}
	}

	abstract class Errors implements Consumer<Throwable> {

		protected final Consumer<Throwable> handler;
		private static final Handling suppress = createHandling(_ -> {
		});
		private static final Rethrowing rethrow = createRethrowing(Errors::rethrowErrorAndWrapOthersAsRuntime);

		protected Errors(Consumer<Throwable> error) {
			this.handler = error;
		}

		public static Handling createHandling(Consumer<Throwable> handler) {
			return new Handling(handler);
		}

		public static Rethrowing createRethrowing(Function<Throwable, RuntimeException> transform) {
			return new Rethrowing(transform);
		}

		public static Handling suppress() {
			return suppress;
		}

		public static Rethrowing rethrow() {
			return rethrow;
		}

		private static RuntimeException rethrowErrorAndWrapOthersAsRuntime(Throwable e) {
			if (e instanceof Error) {
				throw (Error) e;
			} else {
				return Errors.asRuntime(e);
			}
		}

		public static RuntimeException asRuntime(Throwable e) {
			if (e instanceof RuntimeException) {
				return (RuntimeException) e;
			} else {
				return new RuntimeException(e);
			}
		}

		@Override
		public void accept(Throwable error) {
			handler.accept(error);
		}

		public void run(Throwing.Runnable runnable) {
			wrap(runnable).run();
		}

		public Runnable wrap(Throwing.Runnable runnable) {
			return () -> {
				try {
					runnable.run();
				} catch (Throwable e) {
					handler.accept(e);
				}
			};
		}

		public <T> Consumer<T> wrap(Throwing.Consumer<T> consumer) {
			return val -> {
				try {
					consumer.accept(val);
				} catch (Throwable e) {
					handler.accept(e);
				}
			};
		}

		public <T, R> Function<T, R> wrap(Throwing.Function<T, R> function) {
			return input -> {
				try {
					return function.apply(input);
				} catch (Throwable e) {
					handler.accept(e);
					return null;
				}
			};
		}

		public static class Handling extends Errors {
			protected Handling(Consumer<Throwable> error) {
				super(error);
			}

			public <T> T getWithDefault(Throwing.Supplier<T> supplier, T onFailure) {
				return wrapWithDefault(supplier, onFailure).get();
			}

			public <T> Supplier<T> wrapWithDefault(Throwing.Supplier<T> supplier, T onFailure) {
				return () -> {
					try {
						return supplier.get();
					} catch (Throwable e) {
						handler.accept(e);
						return onFailure;
					}
				};
			}

			public <T, R> Function<T, R> wrapWithDefault(Throwing.Function<T, R> function, R onFailure) {
				return wrapFunctionWithDefault(function, onFailure);
			}

			public <T> Predicate<T> wrapWithDefault(Throwing.Predicate<T> predicate, boolean onFailure) {
				return wrapPredicateWithDefault(predicate, onFailure);
			}

			public <T, R> Function<T, R> wrapFunctionWithDefault(Throwing.Function<T, R> function, R onFailure) {
				return input -> {
					try {
						return function.apply(input);
					} catch (Throwable e) {
						handler.accept(e);
						return onFailure;
					}
				};
			}

			public <T> Predicate<T> wrapPredicateWithDefault(Throwing.Predicate<T> predicate, boolean onFailure) {
				return input -> {
					try {
						return predicate.test(input);
					} catch (Throwable e) {
						handler.accept(e);
						return onFailure;
					}
				};
			}
		}

		public static class Rethrowing extends Errors {
			private final Function<Throwable, RuntimeException> transform;

			protected Rethrowing(Function<Throwable, RuntimeException> transform) {
				super(error -> {
					throw transform.apply(error);
				});
				this.transform = transform;
			}

			public <T> T get(Throwing.Supplier<T> supplier) {
				return wrap(supplier).get();
			}

			public <T> Supplier<T> wrap(Throwing.Supplier<T> supplier) {
				return () -> {
					try {
						return supplier.get();
					} catch (Throwable e) {
						throw transform.apply(e);
					}
				};
			}

			public <T, R> Function<T, R> wrap(Throwing.Function<T, R> function) {
				return wrapFunction(function);
			}

			public <T> Predicate<T> wrap(Throwing.Predicate<T> predicate) {
				return wrapPredicate(predicate);
			}

			public <T, R> Function<T, R> wrapFunction(Throwing.Function<T, R> function) {
				return arg -> {
					try {
						return function.apply(arg);
					} catch (Throwable e) {
						throw transform.apply(e);
					}
				};
			}

			public <T> Predicate<T> wrapPredicate(Throwing.Predicate<T> predicate) {
				return arg -> {
					try {
						return predicate.test(arg);
					} catch (Throwable e) {
						throw transform.apply(e); // 1 855 548 2505
					}
				};
			}
		}

	}

}
