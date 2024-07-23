package inc.nomard.spoty.core.views.layout.navigation.event;


import java.util.*;

public abstract class Event {

    protected final UUID id = UUID.randomUUID();

    protected Event() {
    }

    public static <E extends Event> void publish(E event) {
        DefaultEventBus.getInstance().publish(event);
    }

    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Event event)) {
            return false;
        }
        return id.equals(event.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Event{"
                + "id=" + id
                + '}';
    }
}