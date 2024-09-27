package inc.nomard.spoty.core.util.others.observables;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.Supplier;

import inc.nomard.spoty.core.util.observables.OnChanged;
import inc.nomard.spoty.core.util.observables.OnInvalidated;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;

public abstract class When<T> {
    protected static final WeakHashMap<ObservableValue<?>, WeakReference<When<?>>> whens = new WeakHashMap();
    protected final ObservableValue<T> observableValue;
    protected boolean oneShot = false;
    protected final Set<Observable> invalidatingObservables;
    protected InvalidationListener invalidationListener;

    protected When(ObservableValue<T> observableValue) {
        this.observableValue = observableValue;
        this.invalidatingObservables = new HashSet();
        this.invalidationListener = (o) -> {
            this.invalidate();
        };
    }

    public abstract When<T> listen();

    protected final void register() {
        if (whens.containsKey(this.observableValue)) {
            throw new IllegalArgumentException("Cannot register this When construct as the given observable is already being observed");
        } else {
            whens.put(this.observableValue, new WeakReference(this));
        }
    }

    public When<T> invalidating(Observable obs) {
        this.invalidatingObservables.add(obs);
        return this;
    }

    protected When<T> invalidate() {
        return this;
    }

    public When<T> executeNow() {
        return this;
    }

    public When<T> executeNow(Supplier<Boolean> condition) {
        if ((Boolean)condition.get()) {
            this.executeNow();
        }

        return this;
    }

    public boolean isOneShot() {
        return this.oneShot;
    }

    public When<T> oneShot() {
        this.oneShot = true;
        return this;
    }

    protected void dispose() {
        this.invalidatingObservables.forEach((o) -> {
            o.removeListener(this.invalidationListener);
        });
        this.invalidatingObservables.clear();
        if (this.invalidationListener != null) {
            this.invalidationListener = null;
        }

    }

    public static <T> OnInvalidated<T> onInvalidated(ObservableValue<T> observableValue) {
        return OnInvalidated.forObservable(observableValue);
    }

    public static <T> OnChanged<T> onChanged(ObservableValue<T> observableValue) {
        return OnChanged.forObservable(observableValue);
    }

    public static void disposeFor(ObservableValue<?> observableValue) {
        WeakReference<When<?>> ref = (WeakReference)whens.remove(observableValue);
        When<?> remove = ref != null ? (When)ref.get() : null;
        if (remove != null) {
            remove.dispose();
        }

    }
}