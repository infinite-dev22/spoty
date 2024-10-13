package inc.nomard.spoty.core.util.others;

import java.util.Objects;

@FunctionalInterface
public interface TriConsumer<A, B, C> {
    void accept(A var1, B var2, C var3);

    default TriConsumer<A, B, C> andThen(TriConsumer<? super A, ? super B, ? super C> after) {
        Objects.requireNonNull(after);
        return (a, b, c) -> {
            this.accept(a, b, c);
            after.accept(a, b, c);
        };
    }
}