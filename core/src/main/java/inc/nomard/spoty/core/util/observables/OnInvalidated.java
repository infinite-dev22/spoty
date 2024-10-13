package inc.nomard.spoty.core.util.observables;

import java.lang.ref.WeakReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ObservableValue;

public class OnInvalidated<T> extends When<T> {
    private InvalidationListener listener;
    private Consumer<T> action;
    private BiConsumer<WeakReference<When<T>>, T> otherwise = (w, t) -> {
    };
    private Function<T, Boolean> condition = (t) -> {
        return true;
    };

    private OnInvalidated(ObservableValue<T> observableValue) {
        super(observableValue);
    }

    public static <T> OnInvalidated<T> forObservable(ObservableValue<T> observableValue) {
        return new OnInvalidated(observableValue);
    }

    public OnInvalidated<T> then(Consumer<T> action) {
        this.action = action;
        return this;
    }

    public OnInvalidated<T> otherwise(BiConsumer<WeakReference<When<T>>, T> otherwise) {
        this.otherwise = otherwise;
        return this;
    }

    public OnInvalidated<T> condition(Function<T, Boolean> condition) {
        this.condition = condition;
        return this;
    }

    public OnInvalidated<T> executeNow() {
        this.action.accept(this.observableValue.getValue());
        return this;
    }

    public OnInvalidated<T> executeNow(Supplier<Boolean> condition) {
        if ((Boolean)condition.get()) {
            this.executeNow();
        }

        return this;
    }

    public OnInvalidated<T> listen() {
        if (this.oneShot) {
            this.listener = (invalidated) -> {
                T value = this.observableValue.getValue();
                if ((Boolean)this.condition.apply(value)) {
                    this.action.accept(value);
                    this.dispose();
                } else {
                    this.otherwise.accept(new WeakReference(this), value);
                }

            };
        } else {
            this.listener = (invalidated) -> {
                T value = this.observableValue.getValue();
                if ((Boolean)this.condition.apply(value)) {
                    this.action.accept(value);
                } else {
                    this.otherwise.accept(new WeakReference(this), value);
                }

            };
        }

        this.invalidatingObservables.forEach((o) -> {
            o.addListener(this.invalidationListener);
        });
        this.register();
        this.observableValue.addListener(this.listener);
        return this;
    }

    protected When<T> invalidate() {
        this.executeNow(() -> {
            return (Boolean)this.condition.apply(this.observableValue.getValue());
        });
        return this;
    }

    public void dispose() {
        super.dispose();
        if (this.observableValue != null && this.listener != null) {
            this.observableValue.removeListener(this.listener);
            this.listener = null;
            whens.remove(this.observableValue);
        }

    }
}