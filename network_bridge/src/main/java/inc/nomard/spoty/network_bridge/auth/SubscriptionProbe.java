package inc.nomard.spoty.network_bridge.auth;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class SubscriptionProbe {
    public static Boolean canTry;
    public static Boolean blockAccess;
    public static Boolean showTrialSoonEnds;
    public static Boolean showSubscriptionWarning;
    public static Long timeLeft;
    public static String message;
}
