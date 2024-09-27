package inc.nomard.spoty.core.util.enums;


import inc.nomard.spoty.core.util.localization.I18N;

public enum ChainMode {
    AND("&"),
    OR(I18N.getOrDefault("chainMode.or", new Object[0]));

    public static boolean useAlternativeAnd = false;
    private final String text;

    private ChainMode(String text) {
        this.text = text;
    }

    public static boolean chain(ChainMode mode, boolean first, boolean second) {
        return mode == AND ? first && second : first || second;
    }

    public String text() {
        return this == AND && useAlternativeAnd ? I18N.getOrDefault("chainMode.alternativeAnd", new Object[0]) : this.text;
    }
}