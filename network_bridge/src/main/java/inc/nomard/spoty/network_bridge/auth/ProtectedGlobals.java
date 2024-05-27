package inc.nomard.spoty.network_bridge.auth;

import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.*;

public class ProtectedGlobals {
    public static String authToken = "";
    public static boolean trial;
    public static boolean canTry;
    public static boolean newTenancy;
    public static boolean activeTenancy;
    public static String message;
    public static User user;
    public static Role role;
}
