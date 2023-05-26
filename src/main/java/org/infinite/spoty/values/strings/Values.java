package org.infinite.spoty.values.strings;

import java.util.Arrays;
import java.util.LinkedList;

public class Values {
    public static final LinkedList<String> BARCODETYPES = new LinkedList<>(Arrays.stream(new String[]{"Type 01", "Type 02", "Type 03", "Type 04"}).toList());
    public static final LinkedList<String> PURCHASESTATUSES = new LinkedList<>(Arrays.stream(new String[]{"Ordered", "Pending", "Received"}).toList());
}
