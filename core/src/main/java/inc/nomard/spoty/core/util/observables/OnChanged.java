package inc.nomard.spoty.core.util.observables;

import inc.nomard.spoty.core.util.others.TriConsumer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.lang.ref.WeakReference;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class OnChanged<T> extends When<T> {
    private ChangeListener<T> listener;
    private BiConsumer<T, T> action;
    private TriConsumer<WeakReference<When<T>>, T, T> otherwise = (w, o, n) -> {
    };
    private BiFunction<T, T, Boolean> condition = (o, n) -> {
        return true;
    };

    private OnChanged(ObservableValue<T> observableValue) {
        super(observableValue);
    }

    public static <T> OnChanged<T> forObservable(ObservableValue<T> observableValue) {
        return new OnChanged(observableValue);
    }

    public OnChanged<T> then(BiConsumer<T, T> action) {
        this.action = action;
        return this;
    }

    public OnChanged<T> otherwise(TriConsumer<WeakReference<When<T>>, T, T> otherwise) {
        this.otherwise = otherwise;
        return this;
    }

    public OnChanged<T> condition(BiFunction<T, T, Boolean> condition) {
        this.condition = condition;
        return this;
    }

    public OnChanged<T> executeNow() {
        this.action.accept((T) null, this.observableValue.getValue());
        return this;
    }

    public OnChanged<T> executeNow(Supplier<Boolean> condition) {
        if ((Boolean) condition.get()) {
            this.executeNow();
        }

        return this;
    }

    public OnChanged<T> listen() {
        if (this.oneShot) {
            this.listener = (observable, oldValue, newValue) -> {
                if ((Boolean) this.condition.apply(oldValue, newValue)) {
                    this.action.accept(oldValue, newValue);
                    this.dispose();
                } else {
                    this.otherwise.accept(new WeakReference(this), oldValue, newValue);
                }

            };
        } else {
            this.listener = (observable, oldValue, newValue) -> {
                if ((Boolean) this.condition.apply(oldValue, newValue)) {
                    this.action.accept(oldValue, newValue);
                } else {
                    this.otherwise.accept(new WeakReference(this), oldValue, newValue);
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
            return (Boolean) this.condition.apply((T) null, this.observableValue.getValue());
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