package org.infinite.spoty.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;
import java.util.Objects;

public class Category implements Serializable {
    private final StringProperty code = new SimpleStringProperty("");

    private final StringProperty name = new SimpleStringProperty("");

    public String getCategoryCode() {
        return this.code.get();
    }

    public String getCategoryName() {
        return this.name.get();
    }

    public void setCategoryCode(String code) {
        this.code.set(code);
    }

    public void setCategoryName(String name) {
        this.name.set(name);
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof final Category other)) return false;
        if (!other.canEqual(this)) return false;
        final Object this$code = this.getCategoryCode();
        final Object other$code = other.getCategoryCode();
        if (!Objects.equals(this$code, other$code)) return false;
        final Object this$name = this.getCategoryName();
        final Object other$name = other.getCategoryName();
        return Objects.equals(this$name, other$name);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Category;
    }

    public String toString() {
        return "Category(code=" + this.getCategoryCode() + ", name=" + this.getCategoryName() + ")";
    }
}
