package inc.nomard.spoty.core.values.strings;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.extern.log4j.Log4j2;

import java.util.Arrays;

@Log4j2
public class Values {
    public static final ObservableList<String> BARCODE_TYPES =
            FXCollections.observableArrayList(Arrays.stream(new String[]{"Code128", "Code39", "EAN8", "EAN13", "UPC A", "UPC E"}).toList());
    public static final ObservableList<String> ADJUSTMENT_TYPE =
            FXCollections.observableArrayList(Arrays.stream(new String[]{"Increment", "Decrement"}).toList());
    public static final int FIELD_HEIGHT = 35;
}
