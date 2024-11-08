package inc.nomard.spoty.core.views.layout.navigation;

import inc.nomard.spoty.core.views.util.Page;
import javafx.scene.Node;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

record Nav(
        String title, String tag, Node graphic, Class<? extends Page> view, List<String> searchKeywords) {

    // Treeview parent not.
    public static final Nav ROOT = new Nav("ROOT", null, null, null, null);

    public Nav {
        Objects.requireNonNull(title, "title");
        searchKeywords = Objects.requireNonNullElse(searchKeywords, Collections.emptyList());
    }

    public boolean isGroup() {
        return Objects.equals(view, null);
    }

    public boolean isInnerGroup() {
        return Objects.equals(graphic, null) && Objects.equals(view, null);
    }

    public boolean isMainPage() {
        return !Objects.equals(view, null) && !Objects.equals(graphic, null);
    }

    public boolean matches(String filter) {
        Objects.requireNonNull(filter);
        return contains(title, filter)
                || (searchKeywords != null
                && searchKeywords.stream().anyMatch(keyword -> contains(keyword, filter)));
    }

    private boolean contains(String text, String filter) {
        return text.toLowerCase().contains(filter.toLowerCase());
    }
}
