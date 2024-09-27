package inc.nomard.spoty.utils;

import java.text.DecimalFormat;

public class AppUtils {
    public static DecimalFormat decimalFormatter() {
        return new DecimalFormat("###,###,###,###,###,###,###,###,###,###.##");
    }
}
