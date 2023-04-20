package org.infinite.spoty.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;
import java.util.Objects;

public class RoleMaster implements Serializable {
    private final StringProperty role = new SimpleStringProperty("");
    private final StringProperty description = new SimpleStringProperty("");

    public String getRole() {
        return this.role.get();
    }

    public String getDescription() {
        return this.description.get();
    }

    public void setRole(String role) {
        this.role.set(role);
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof final RoleMaster other)) return false;
        if (!other.canEqual(this)) return false;
        final Object this$role = this.getRole();
        final Object other$role = other.getRole();
        if (!Objects.equals(this$role, other$role)) return false;
        final Object this$description = this.getDescription();
        final Object other$description = other.getDescription();
        return Objects.equals(this$description, other$description);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof RoleMaster;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $role = this.getRole();
        result = result * PRIME + ($role == null ? 43 : $role.hashCode());
        final Object $description = this.getDescription();
        result = result * PRIME + ($description == null ? 43 : $description.hashCode());
        return result;
    }

    public String toString() {
        return "RoleMaster(role=" + this.getRole() + ", description=" + this.getDescription() + ")";
    }
}
