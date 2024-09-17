package inc.nomard.spoty.core.values.strings;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.extern.java.Log;

import java.util.Arrays;

@Log
public class Values {
    public static final ObservableList<String> BARCODE_TYPES =
            FXCollections.observableArrayList(Arrays.stream(new String[]{"Code128", "Code39", "EAN8", "EAN13", "UPC A", "UPC E"}).toList());
    public static final ObservableList<String> PRODUCT_TYPES =
            FXCollections.observableArrayList(Arrays.stream(new String[]{"Product", "Service"}).toList());
    public static final ObservableList<String> PURCHASE_STATUSES =
            FXCollections.observableArrayList(Arrays.stream(new String[]{"Approved", "Pending", "Rejected", "Returned"}).toList());
    public static final ObservableList<String> SALE_STATUSES =
            FXCollections.observableArrayList(Arrays.stream(new String[]{"Ordered", "Pending", "Completed", "Returned"}).toList());
    public static final ObservableList<String> PAYMENT_STATUSES =
            FXCollections.observableArrayList(Arrays.stream(new String[]{"Partial", "Pending", "Paid"}).toList());
    public static final ObservableList<String> ADJUSTMENT_TYPE =
            FXCollections.observableArrayList(Arrays.stream(new String[]{"Increment", "Decrement"}).toList());
    public static final ObservableList<String> QUOTATION_TYPE =
            FXCollections.observableArrayList(Arrays.stream(new String[]{"Sent", "Pending", "Closed", "Cancelled"}).toList());
    public static final String[] SPECIALS = "@ . ".split(" ");
    public static final String[] CHARACTERS = "@ _ - £ $ % ^ & * ( ) \" ! / \\ > < ? | ' # ~ ¬ ` : ; [ ] { } + =".split(" ");
    public static final String[] ALPHANUMERIC =
            "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z a b c d e f g h i j k l m n o p q r s t u v w x y z 0 1 2 3 4 5 6 7 8 9"
                    .split(" ");
}
