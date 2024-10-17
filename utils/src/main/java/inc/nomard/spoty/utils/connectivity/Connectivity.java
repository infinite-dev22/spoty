package inc.nomard.spoty.utils.connectivity;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetAddress;

@Slf4j
public class Connectivity {
    public static boolean isConnectedToInternet() {
        try {
            return InetAddress.getByName("google.com").isReachable(1000); // Check reachability with a timeout of 5 seconds
        } catch (IOException e) {
            log.error("No internet connection", e);
            return false;
        }
    }
}
