package inc.nomard.spoty.responsive_fx.layouts;

public enum Breakpoint {
    XSMALL(0),
    SMALL(1),
    MEDIUM(2),
    LARGE(3),
    XLARGE(4);

    private int value;

    Breakpoint(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
