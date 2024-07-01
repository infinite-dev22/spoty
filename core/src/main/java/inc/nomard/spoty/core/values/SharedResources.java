package inc.nomard.spoty.core.values;

import java.util.*;
import javafx.beans.property.*;
import lombok.extern.java.*;

@Log
public class SharedResources {
    public static final LinkedList<Long> PENDING_DELETES = new LinkedList<>();
    private static final LongProperty TEMP_ID = new SimpleLongProperty(-1);

    public static int getTempId() {
        return (int) TEMP_ID.get();
    }

    public static void setTempId(long tempId) {
        SharedResources.TEMP_ID.set(tempId);
    }

    public static LongProperty tempIdProperty() {
        return TEMP_ID;
    }
}
