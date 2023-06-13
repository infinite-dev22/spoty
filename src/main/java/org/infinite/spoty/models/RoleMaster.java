/*
 * Copyright (c) 2023, Jonathan Mark Mwigo. All rights reserved.
 *
 * The computer system code contained in this file is the property of Jonathan Mark Mwigo and is protected by copyright law. Any unauthorized use of this code is prohibited.
 *
 * This copyright notice applies to all parts of the computer system code, including the source code, object code, and any other related materials.
 *
 * The computer system code may not be modified, translated, or reverse-engineered without the express written permission of Jonathan Mark Mwigo.
 *
 * Jonathan Mark Mwigo reserves the right to update, modify, or discontinue the computer system code at any time.
 *
 * Jonathan Mark Mwigo makes no warranties, express or implied, with respect to the computer system code. Jonathan Mark Mwigo shall not be liable for any damages, including, but not limited to, direct, indirect, incidental, special, consequential, or punitive damages, arising out of or in connection with the use of the computer system code.
 */

package org.infinite.spoty.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;
import java.util.Objects;

@Deprecated
public class RoleMaster implements Serializable {
    private final StringProperty role = new SimpleStringProperty("");
    private final StringProperty description = new SimpleStringProperty("");

    public String getRole() {
        return this.role.get();
    }

    public void setRole(String role) {
        this.role.set(role);
    }

    public String getDescription() {
        return this.description.get();
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
