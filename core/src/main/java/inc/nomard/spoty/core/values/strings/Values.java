package inc.nomard.spoty.core.values.strings;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Values {
    public static final ObservableList<String> BARCODE_TYPES =
            FXCollections.observableArrayList("Code128", "Code39", "EAN8", "EAN13", "UPC A", "UPC E");
    public static final ObservableList<String> ADJUSTMENT_TYPE =
            FXCollections.observableArrayList("Increment", "Decrement");
    public static final int FIELD_HEIGHT = 35;
}
