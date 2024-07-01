package inc.nomard.spoty.core.views.layout.message.enums;

public enum MessageDuration {
    SHORT(3000),
    MEDIUM(5000),
    LONG(7000);

    private final int duration;

    MessageDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }
}
