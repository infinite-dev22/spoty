package org.infinite.spoty.model;

import java.io.Serializable;
import java.util.Objects;

public class Category implements Serializable {
    private String code;
    private String name;

    public String getCategoryCode() {
        return this.code;
    }

    public String getCategoryName() {
        return this.name;
    }

    public void setCategoryCode(String code) {
        this.code = code;
    }

    public void setCategoryName(String name) {
        this.name = name;
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

    public int hashCategoryCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $code = this.getCategoryCode();
        result = result * PRIME + ($code == null ? 43 : $code.hashCode());
        final Object $name = this.getCategoryName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        return result;
    }

    public String toString() {
        return "Category(code=" + this.getCategoryCode() + ", name=" + this.getCategoryName() + ")";
    }
}
