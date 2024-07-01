package inc.nomard.spoty.core.values.strings;

import java.util.*;
import lombok.extern.java.*;

@Log
public class Values {
    public static final LinkedList<String> BARCODE_TYPES =
            new LinkedList<>(
                    Arrays.stream(new String[]{"Code128", "Code39", "EAN8", "EAN13", "UPC A", "UPC E"}).toList());
    public static final LinkedList<String> PRODUCT_TYPES =
            new LinkedList<>(
                    Arrays.stream(new String[]{"Product", "Service"}).toList());
    public static final LinkedList<String> PURCHASE_STATUSES =
            new LinkedList<>(Arrays.stream(new String[]{"Ordered", "Pending", "Received", "Returned"}).toList());
    public static final LinkedList<String> SALE_STATUSES =
            new LinkedList<>(Arrays.stream(new String[]{"Ordered", "Pending", "Completed", "Returned"}).toList());
    public static final LinkedList<String> PAYMENT_STATUSES =
            new LinkedList<>(Arrays.stream(new String[]{"Partial", "Pending", "Paid"}).toList());
    public static final LinkedList<String> ADJUSTMENT_TYPE =
            new LinkedList<>(Arrays.stream(new String[]{"Increment", "Decrement"}).toList());
    public static final LinkedList<String> QUOTATION_TYPE =
            new LinkedList<>(Arrays.stream(new String[]{"Sent", "Pending", "Closed", "Cancelled"}).toList());
    public static final String[] SPECIALS = "@ . ".split(" ");
    public static final String[] CHARACTERS = "@ _ - £ $ % ^ & * ( ) \" ! / \\ > < ? | ' # ~ ¬ ` : ; [ ] { } + =".split(" ");
    public static final String[] ALPHANUMERIC =
            "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z a b c d e f g h i j k l m n o p q r s t u v w x y z 0 1 2 3 4 5 6 7 8 9"
                    .split(" ");
}
