package inc.nomard.spoty.utils.connectivity;

import java.io.IOException;
import java.net.InetAddress;

public class Connectivity {
    public static boolean isConnectedToInternet() {
        try {
            return InetAddress.getByName("google.com").isReachable(1000); // Check reachability with a timeout of 5 seconds
        } catch (IOException e) {
            return false;
        }
    }
}
