package inc.nomard.spoty.utils.responsiveness.layouts;

public enum Breakpoint {
    XSMALL(0),
    SMALL(1),
    MEDIUM(2),
    LARGE(3),
    XLARGE(4);

    private final int value;

    Breakpoint(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
