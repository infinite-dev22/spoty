package inc.nomard.spoty.core.views.util;

import javafx.util.StringConverter;

import java.util.function.Function;

/**
 * A functional alternative to {@link StringConverter}.
 */
@FunctionalInterface
public interface FunctionalStringConverter<T> {

    /**
     * @return a new {@link StringConverter} which uses the given function
     * to convert a String to an object of type T
     */
    static <T> StringConverter<T> converter(Function<String, T> fsFunction) {
        return new StringConverter<>() {
            @Override
            public String toString(T t) {
                throw new UnsupportedOperationException();
            }

            @Override
            public T fromString(String string) {
                return fsFunction.apply(string);
            }
        };
    }

    /**
     * @return a new {@link StringConverter} which uses the given functions
     * to convert a String to an object of type T and vice versa.
     */
    static <T> StringConverter<T> converter(Function<String, T> fsFunction, Function<T, String> tsFunction) {
        return new StringConverter<>() {
            @Override
            public String toString(T t) {
                return t != null ? tsFunction.apply(t) : "";
            }

            @Override
            public T fromString(String string) {
                return fsFunction.apply(string);
            }
        };
    }

    /**
     * @return a new {@link StringConverter} which is only capable of converting a String
     * to an object of type T
     * @throws UnsupportedOperationException when using the toString(T) method
     */
    static <T> StringConverter<T> from(Function<String, T> fsFunction) {
        return new StringConverter<>() {
            @Override
            public String toString(T t) {
                throw new UnsupportedOperationException();
            }

            @Override
            public T fromString(String string) {
                return fsFunction.apply(string);
            }
        };
    }

    /**
     * @return a new {@link StringConverter} which is only capable of converting an object
     * of type T to a String
     * @throws UnsupportedOperationException when using the fromString(String) method
     */
    static <T> StringConverter<T> to(Function<T, String> tsFunction) {
        return new StringConverter<>() {
            @Override
            public String toString(T t) {
                return tsFunction.apply(t);
            }

            @Override
            public T fromString(String string) {
                throw new UnsupportedOperationException();
            }
        };
    }

    /**
     * Converts the given {@code String} to an object of type {@code T}
     */
    T fromString(String s);

    /**
     * Default implementation throws {@link UnsupportedOperationException}.
     */
    default String toString(T t) {
        throw new UnsupportedOperationException();
    }
}