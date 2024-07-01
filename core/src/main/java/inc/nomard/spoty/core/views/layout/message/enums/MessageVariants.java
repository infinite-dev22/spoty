package inc.nomard.spoty.core.views.layout.message.enums;

public enum MessageVariants {
    ERROR("error"),
    SUCCESS("success"),
    WARN("warn"),
    INFO("info");

    private final String styleClass;

    MessageVariants(String styleClass) {
        this.styleClass = styleClass;
    }

    public String variantStyleClass() {
        return this.styleClass;
    }

    public String getStyleClass() {
        return styleClass;
    }
}
