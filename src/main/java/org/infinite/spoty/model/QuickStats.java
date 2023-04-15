package org.infinite.spoty.model;

import java.io.Serializable;
import java.util.Objects;

public class QuickStats implements Serializable {
    private String title;
    private String subtitle;

    public String getTitle() {
        return this.title;
    }

    public String getSubtitle() {
        return this.subtitle;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof final QuickStats other)) return false;
        if (!other.canEqual(this)) return false;
        final Object this$title = this.getTitle();
        final Object other$title = other.getTitle();
        if (!Objects.equals(this$title, other$title)) return false;
        final Object this$subtitle = this.getSubtitle();
        final Object other$subtitle = other.getSubtitle();
        return Objects.equals(this$subtitle, other$subtitle);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof QuickStats;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $title = this.getTitle();
        result = result * PRIME + ($title == null ? 43 : $title.hashCode());
        final Object $subtitle = this.getSubtitle();
        result = result * PRIME + ($subtitle == null ? 43 : $subtitle.hashCode());
        return result;
    }

    public String toString() {
        return "QuickStats(title=" + this.getTitle() + ", subtitle=" + this.getSubtitle() + ")";
    }
}
