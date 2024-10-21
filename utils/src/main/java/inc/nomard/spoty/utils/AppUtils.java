package inc.nomard.spoty.utils;

import java.nio.file.Paths;
import java.text.DecimalFormat;

public class AppUtils {
    public static DecimalFormat decimalFormatter() {
        return new DecimalFormat("###,###,###,###,###,###,###,###,###,###.##");
    }

    public static String getInstallationDirectory() {
        try {
            var currentRelativePath = Paths.get("");
            return currentRelativePath.toAbsolutePath().toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
