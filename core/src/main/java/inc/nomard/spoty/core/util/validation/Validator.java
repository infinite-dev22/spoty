package inc.nomard.spoty.core.util.validation;

import inc.nomard.spoty.core.util.enums.ChainMode;
import inc.nomard.spoty.core.util.observables.When;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;

public class Validator {
    private final ObservableList<Constraint> constraints = FXCollections.observableArrayList();
    private final ObservableList<Validator> dependencies = FXCollections.observableArrayList();
    private final ReadOnlyBooleanWrapper valid = new ReadOnlyBooleanWrapper(true);
    private BiConsumer<Boolean, List<Constraint>> onUpdated;
    private boolean sortBySeverity = true;
    private boolean failFast = false;

    public Validator() {
        this.constraints.addListener((ListChangeListener<? super Constraint>) (invalidated) -> this.update());
        this.dependencies.addListener((ListChangeListener<? super Validator>) (invalidated) -> this.update());
    }

    public Validator constraint(Constraint constraint) {
        When.onInvalidated(constraint.getCondition()).then((value) -> {
            this.update();
        }).listen();
        this.constraints.add(constraint);
        return this;
    }

    public Validator constraint(Severity severity, String message, BooleanExpression condition) {
        return this.constraint(Constraint.of(severity, message, condition));
    }

    public Validator constraint(String message, BooleanExpression condition) {
        return this.constraint(Severity.ERROR, message, condition);
    }

    public Validator removeConstraint(Constraint constraint) {
        if (this.constraints.remove(constraint)) {
            When.disposeFor(constraint.getCondition());
        }

        return this;
    }

    public Validator dependsOn(Validator validator) {
        When.onInvalidated(validator.validProperty()).then((value) -> {
            this.update();
        }).listen();
        this.dependencies.add(validator);
        return this;
    }

    public Validator removeDependency(Validator validator) {
        if (this.dependencies.remove(validator)) {
            When.disposeFor(validator.validProperty());
        }

        return this;
    }

    public List<Constraint> validate() {
        List<Constraint> invalidConstraints = new ArrayList();
        Iterator var2 = this.dependencies.iterator();

        while (var2.hasNext()) {
            Validator dependency = (Validator) var2.next();
            if (!dependency.isValid()) {
                if (this.failFast) {
                    return List.of((Constraint) dependency.validate().get(0));
                }

                invalidConstraints.addAll(dependency.validate());
            }
        }

        var2 = this.constraints.iterator();

        while (var2.hasNext()) {
            Constraint constraint = (Constraint) var2.next();
            if (!constraint.isValid()) {
                invalidConstraints.add(constraint);
                if (this.failFast) {
                    return invalidConstraints;
                }
            }
        }

        if (this.sortBySeverity) {
            invalidConstraints.sort(Comparator.comparing(Constraint::getSeverity));
        }

        return invalidConstraints;
    }

    public void update() {
        boolean valid = true;

        Iterator var2;
        Validator dependency;
        for (var2 = this.dependencies.iterator(); var2.hasNext(); valid = valid && dependency.isValid()) {
            dependency = (Validator) var2.next();
        }

        Constraint constraint;
        for (var2 = this.constraints.iterator(); var2.hasNext(); valid = ChainMode.chain(constraint.getChainMode(), valid, constraint.isValid())) {
            constraint = (Constraint) var2.next();
        }

        this.setValid(valid);
        this.onUpdated();
    }

    public String validateToString() {
        List<Constraint> invalidConstraints = this.validate();
        if (invalidConstraints.isEmpty()) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            invalidConstraints.forEach((constraint) -> {
                sb.append(constraint.getMessage()).append("\n");
            });
            return sb.toString();
        }
    }

    protected void onUpdated() {
        if (this.onUpdated != null) {
            this.onUpdated.accept(this.isValid(), this.validate());
        }

    }

    public boolean isValid() {
        return this.valid.get();
    }

    protected void setValid(boolean valid) {
        this.valid.set(valid);
    }

    public ReadOnlyBooleanProperty validProperty() {
        return this.valid.getReadOnlyProperty();
    }

    public BiConsumer<Boolean, List<Constraint>> getOnUpdated() {
        return this.onUpdated;
    }

    public Validator setOnUpdated(BiConsumer<Boolean, List<Constraint>> onUpdated) {
        this.onUpdated = onUpdated;
        return this;
    }

    public boolean isSortBySeverity() {
        return this.sortBySeverity;
    }

    public Validator setSortBySeverity(boolean sortBySeverity) {
        this.sortBySeverity = sortBySeverity;
        return this;
    }

    public boolean isFailFast() {
        return this.failFast;
    }

    public Validator setFailFast(boolean failFast) {
        this.failFast = failFast;
        return this;
    }
}