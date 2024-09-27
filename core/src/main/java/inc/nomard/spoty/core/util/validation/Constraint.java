package inc.nomard.spoty.core.util.validation;

import inc.nomard.spoty.core.util.enums.ChainMode;
import javafx.beans.binding.BooleanExpression;

public class Constraint {
    private Severity severity;
    private String message;
    private BooleanExpression condition;
    private ChainMode chainMode;

    protected Constraint() {
        this.chainMode = ChainMode.AND;
    }

    public Constraint(String message, BooleanExpression condition) {
        this(Severity.ERROR, message, condition);
    }

    public Constraint(Severity severity, String message, BooleanExpression condition) {
        this.chainMode = ChainMode.AND;
        if (condition == null) {
            throw new NullPointerException("The condition cannot be null!");
        } else {
            this.severity = severity;
            this.message = message;
            this.condition = condition;
        }
    }

    public static Constraint of(String message, BooleanExpression condition) {
        return new Constraint(message, condition);
    }

    public static Constraint of(Severity severity, String message, BooleanExpression condition) {
        return new Constraint(severity, message, condition);
    }

    public boolean isValid() {
        return this.condition.getValue();
    }

    public Severity getSeverity() {
        return this.severity;
    }

    protected void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public String getMessage() {
        return this.message;
    }

    protected void setMessage(String message) {
        this.message = message;
    }

    public BooleanExpression getCondition() {
        return this.condition;
    }

    protected void setCondition(BooleanExpression condition) {
        this.condition = condition;
    }

    public ChainMode getChainMode() {
        return this.chainMode;
    }

    public Constraint setChainMode(ChainMode chainMode) {
        this.chainMode = chainMode;
        return this;
    }

    public static class Builder {
        private final Constraint constraint = new Constraint();

        public Builder() {
        }

        public static Builder build() {
            return new Builder();
        }

        public Builder setSeverity(Severity severity) {
            this.constraint.setSeverity(severity);
            return this;
        }

        public Builder setMessage(String message) {
            this.constraint.setMessage(message);
            return this;
        }

        public Builder setCondition(BooleanExpression condition) {
            this.constraint.setCondition(condition);
            return this;
        }

        public Builder setChainMode(ChainMode mode) {
            this.constraint.setChainMode(mode);
            return this;
        }

        public Constraint get() {
            this.checkConstraint();
            return this.constraint;
        }

        private void checkConstraint() {
            Severity severity = this.constraint.getSeverity();
            BooleanExpression condition = this.constraint.getCondition();
            if (severity == null) {
                throw new IllegalArgumentException("Severity not set!");
            } else if (condition == null) {
                throw new IllegalArgumentException("Condition not set!");
            }
        }
    }
}
