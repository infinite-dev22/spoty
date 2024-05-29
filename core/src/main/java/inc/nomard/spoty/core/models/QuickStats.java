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

package inc.nomard.spoty.core.models;

import java.util.*;
import lombok.extern.slf4j .*;

@Deprecated
@Slf4j
public class QuickStats {
    private String title;
    private String subtitle;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return this.subtitle;
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