package inc.nomard.spoty.core.values;


import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import lombok.extern.log4j.Log4j2;

import java.util.LinkedList;

@Log4j2
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
